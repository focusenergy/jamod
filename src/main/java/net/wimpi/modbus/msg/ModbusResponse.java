/***
 * Copyright 2002-2010 jamod development team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Original implementation by jamod development team.
 * This file modified by Charles Hache <chache@brood.ca>
 ***/

package net.wimpi.modbus.msg;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.ProcessImage;

/**
 * Abstract class implementing a <tt>ModbusResponse</tt>. This class provides
 * specialised implementations with the functionality they have in common.
 * 
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public abstract class ModbusResponse extends ModbusMessageImpl {

	protected ProcessImage processImage = null;

	/**
	 * Utility method to set the raw data of the message. Should not be used
	 * except under rare circumstances.
	 * <p>
	 * 
	 * @param msg
	 *            the <tt>byte[]</tt> resembling the raw modbus response
	 *            message.
	 */
	protected void setMessage(byte[] msg) {
		try {
			readData(new DataInputStream(new ByteArrayInputStream(msg)));
		} catch (IOException ex) {

		}
	}// setMessage

	/**
	 * Factory method creating the required specialized <tt>ModbusResponse</tt>
	 * instance.
	 * 
	 * @param functionCode
	 *            the function code of the response as <tt>int</tt>.
	 * @return a ModbusResponse instance specific for the given function code.
	 */
	public static ModbusResponse createModbusResponse(int functionCode) {
		ModbusResponse response = null;

		switch (functionCode) {
		case Modbus.READ_MULTIPLE_REGISTERS:
			response = new ReadMultipleRegistersResponse();
			break;
		case Modbus.READ_INPUT_DISCRETES:
			response = new ReadInputDiscretesResponse();
			break;
		case Modbus.READ_INPUT_REGISTERS:
			response = new ReadInputRegistersResponse();
			break;
		case Modbus.READ_COILS:
			response = new ReadCoilsResponse();
			break;
		case Modbus.WRITE_MULTIPLE_REGISTERS:
			response = new WriteMultipleRegistersResponse();
			break;
		case Modbus.WRITE_SINGLE_REGISTER:
			response = new WriteSingleRegisterResponse();
			break;
		case Modbus.WRITE_COIL:
			response = new WriteCoilResponse();
			break;
		case Modbus.WRITE_MULTIPLE_COILS:
			response = new WriteMultipleCoilsResponse();
			break;
		default:
			response = new ExceptionResponse();
			break;
		}
		return response;
	}// createModbusResponse

	/**
	 * Set the process image used for the response generation. When the
	 * transport creates responses, it will use the ProcessImageFactory from
	 * this ProcessImage. If this function hasn't been called, or has been
	 * called with null, then the request will use the default process image set
	 * in the ModbusCoupler.
	 * 
	 * @param procImg
	 *            The process image to use when generating the response, or null
	 *            to use the default process image set in the ModbusCoupler.
	 */
	public void setProcessImage(ProcessImage procImg) {
		processImage = procImg;
	}

	/**
	 * Get the ProcessImage associated with this response. If this response's
	 * process image has not been set, then it returns the globally set process
	 * image from the ModbusCoupler. If neither are set, returns null.
	 * 
	 * @return The process image for this response or null if it, as well as the
	 *         global ModbusCoupler's process image, have not been set.
	 */
	public ProcessImage getProcessImage() {
		if (this.processImage == null)
			return ModbusCoupler.getReference().getProcessImage();
		else
			return this.processImage;
	}

}// class ModbusResponse
