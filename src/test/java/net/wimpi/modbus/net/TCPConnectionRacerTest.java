package net.wimpi.modbus.net;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.procimg.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TCPConnectionRacerTest {

    private static SimpleProcessImage spi;
    private static ModbusTCPListener listener;
    private static TCPMasterConnection connection;

    @BeforeClass
    public static void setup() throws Exception {
        System.out.println("Setting up modbus pair");
        spi = new SimpleProcessImage();
        spi.addDigitalIn(new SimpleDigitalIn(false));
        spi.addDigitalIn(new SimpleDigitalIn(true));
        spi.addDigitalOut(new SimpleDigitalOut(false));
        spi.addDigitalOut(new SimpleDigitalOut(true));
        spi.addInputRegister(new SimpleInputRegister(1));
        spi.addInputRegister(new SimpleInputRegister(2));
        spi.addRegister(new SimpleRegister(1));
        spi.addRegister(new SimpleRegister(2));

        ModbusCoupler.getReference().setProcessImage(spi);
        ModbusCoupler.getReference().setMaster(false);
        ModbusCoupler.getReference().setUnitID(0);

        listener = new ModbusTCPListener(10);
        listener.setAddress(InetAddress.getLoopbackAddress());
        listener.setPort(5502);
        listener.start();

        Thread.sleep(1000);

        connection = new TCPMasterConnection(InetAddress.getLoopbackAddress());
        connection.setPort(5502);
        connection.connect();
    }

    @AfterClass
    public static void cleanup() {
        if (connection != null) {
            connection.close();
        }
        if (listener != null) {
            listener.stop();
        }
    }

    @Test
    public void testReadsWrites() throws ModbusException {
        System.out.println("Test reads/writes single thread");
        int[] expected = {1, 2};
        for (int i = 0; i < 100000; i++) {
            if (i % 11 == 0) {
                expected[0] = i % 1024;
                expected[1] = (i % 1024) + 1;
                writeRegisters(0, expected[0], expected[1]);
            }
            int[] actual = readRegisters(0);
            assertArrayEquals("register read did not return expected value", expected, actual);
        }
    }

    @Test
    public void testReadsThreadedWritesFails() throws ModbusException {
        System.out.println("Test reads / threaded writes, expecting some form of failure");
        boolean exceptionFound = false;
        for (int i = 0; i < 1000; i++) {
            if (i % 11 == 0) {
                final int[] registers = {i, i + 1};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            writeRegisters(0, registers[0], registers[1]);
                        } catch (ModbusException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            try {
                readRegisters(0);
            } catch (ModbusIOException | ModbusSlaveException | ClassCastException e) {
                e.printStackTrace();
                exceptionFound = true;
            }
        }
        assertTrue(exceptionFound);
    }

    private static int[] readRegisters(int register) throws ModbusException {
        ModbusTCPTransaction tx = new ModbusTCPTransaction(connection);
        tx.setRequest(new ReadMultipleRegistersRequest(register, 2));
        tx.execute();
        ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) tx.getResponse();
        int[] registers = {response.getRegisterValue(0), response.getRegisterValue(1)};
        return registers;
    }

    private static void writeRegisters(int offset, int value1, int value2) throws ModbusException {
        Register[] registers = {new SimpleRegister(value1), new SimpleRegister(value2)};
        ModbusTCPTransaction tx = new ModbusTCPTransaction(connection);
        tx.setRequest(new WriteMultipleRegistersRequest(offset, registers));
        tx.execute();
    }
}
