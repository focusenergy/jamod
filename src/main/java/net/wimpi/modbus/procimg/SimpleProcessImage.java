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

package net.wimpi.modbus.procimg;

import java.util.Vector;

/**
 * Class implementing a simple process image to be able to run unit tests or
 * handle simple cases.
 * <p>
 * By default the process image will allow jamod slaves to respond to requests
 * with any unit ID. If the process image's unit ID is set with
 * {@link #setUnitId(Integer)} then the jamod slave will not respond unless the
 * request's unit ID matches the process image's.
 * 
 * @author Dieter Wimberger
 * @author Charles Hache
 * @version @version@ (@date@)
 */
public class SimpleProcessImage implements ProcessImage {

	// instance attributes
	protected Vector<DigitalIn> m_DigitalInputs;
	protected Vector<DigitalOut> m_DigitalOutputs;
	protected Vector<InputRegister> m_InputRegisters;
	protected Vector<Register> m_Registers;
	protected Integer m_UnitId = null;
	protected boolean m_Locked = false;
	protected ProcessImageFactory m_Factory = DefaultProcessImageFactory.getReference();

	/**
	 * Constructs a new <tt>SimpleProcessImage</tt> instance.
	 */
	public SimpleProcessImage() {
		m_DigitalInputs = new Vector<DigitalIn>();
		m_DigitalOutputs = new Vector<DigitalOut>();
		m_InputRegisters = new Vector<InputRegister>();
		m_Registers = new Vector<Register>();
	}// SimpleProcessImage

	public boolean isLocked() {
		return m_Locked;
	}// isLocked

	public void setLocked(boolean locked) {
		m_Locked = locked;
	}// setLocked

	public void addDigitalIn(DigitalIn di) {
		if (!isLocked()) {
			m_DigitalInputs.addElement(di);
		}
	}// addDigitalIn

	public void removeDigitalIn(DigitalIn di) {
		if (!isLocked()) {
			m_DigitalInputs.removeElement(di);
		}
	}// removeDigitalIn

	/**
	 * Set the unit ID for this process image. When the unit ID is not set, or
	 * is set to null, the process image will allow the jamod slave to respond
	 * to all requests.
	 * <p>
	 * If the unit ID is explicitly set, then the process image will not allow a
	 * jamod slave to send a response unless the requests was addressed with the
	 * correct unit ID.
	 * 
	 * @param unitId
	 */
	public void setUnitId(Integer unitId) {
		m_UnitId = unitId;
	}

	public Integer getUnitId() {
		return m_UnitId;
	}

	private void checkUnitId(int unitId) throws InvalidUnitIDException {
		if (m_UnitId != null) {
			if (unitId != m_UnitId) {
				throw new InvalidUnitIDException();
			}
		}
	}

	@Override
	public DigitalIn getDigitalIn(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		try {
			return m_DigitalInputs.elementAt(ref);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalAddressException();
		}
	}// getDigitalIn

	@Override
	public DigitalIn[] getDigitalInRange(int unitId, int ref, int count)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		// ensure valid reference range
		if (ref < 0 || ref + count > m_DigitalInputs.size()) {
			throw new IllegalAddressException();
		} else {
			DigitalIn[] dins = new DigitalIn[count];
			for (int i = 0; i < dins.length; i++) {
				dins[i] = getDigitalIn(unitId, ref + i);
			}
			return dins;
		}
	}// getDigitalInRange

	public void addDigitalOut(DigitalOut _do) {
		if (!isLocked()) {
			m_DigitalOutputs.addElement(_do);
		}
	}// addDigitalOut

	public void removeDigitalOut(DigitalOut _do) {
		if (!isLocked()) {
			m_DigitalOutputs.removeElement(_do);
		}
	}// removeDigitalOut

	@Override
	public DigitalOut getDigitalOut(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		try {
			return m_DigitalOutputs.elementAt(ref);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalAddressException();
		}
	}// getDigitalOut

	@Override
	public DigitalOut[] getDigitalOutRange(int unitId, int ref, int count)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		// ensure valid reference range
		if (ref < 0 || ref + count > m_DigitalOutputs.size()) {
			throw new IllegalAddressException();
		} else {
			DigitalOut[] douts = new DigitalOut[count];
			for (int i = 0; i < douts.length; i++) {
				douts[i] = getDigitalOut(unitId, ref + i);
			}
			return douts;
		}
	}// getDigitalOutRange

	public void addInputRegister(InputRegister reg) {
		if (!isLocked()) {
			m_InputRegisters.addElement(reg);
		}
	}// addInputRegister

	public void removeInputRegister(InputRegister reg) {
		if (!isLocked()) {
			m_InputRegisters.removeElement(reg);
		}
	}// removeInputRegister

	@Override
	public InputRegister getInputRegister(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		try {
			return m_InputRegisters.elementAt(ref);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalAddressException();
		}
	}// getInputRegister

	@Override
	public InputRegister[] getInputRegisterRange(int unitId, int ref, int count)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		// ensure valid reference range
		if (ref < 0 || ref + count > m_InputRegisters.size()) {
			throw new IllegalAddressException();
		} else {
			InputRegister[] iregs = new InputRegister[count];
			for (int i = 0; i < iregs.length; i++) {
				iregs[i] = getInputRegister(unitId, ref + i);
			}
			return iregs;
		}
	}// getInputRegisterRange

	public void addRegister(Register reg) {
		if (!isLocked()) {
			m_Registers.addElement(reg);
		}
	}// addRegister

	public void removeRegister(Register reg) {
		if (!isLocked()) {
			m_Registers.removeElement(reg);
		}
	}// removeRegister

	@Override
	public Register getRegister(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		try {
			return m_Registers.elementAt(ref);
		} catch (IndexOutOfBoundsException ex) {
			throw new IllegalAddressException();
		}
	}// getRegister

	@Override
	public Register[] getRegisterRange(int unitId, int ref, int count)
			throws IllegalAddressException, InvalidUnitIDException {

		checkUnitId(unitId);

		// ensure valid reference range
		if (ref < 0 || ref + count > m_Registers.size()) {
			throw new IllegalAddressException();
		} else {
			Register[] iregs = new Register[count];
			for (int i = 0; i < iregs.length; i++) {
				iregs[i] = getRegister(unitId, ref + i);
			}
			return iregs;
		}
	}// getRegisterRange

	@Override
	public ProcessImageFactory getProcessImageFactory() {
		return m_Factory;
	}
	
	public void setProcessImageFactory(ProcessImageFactory pf) {
		m_Factory = pf;
	}

}// class SimpleProcessImage
