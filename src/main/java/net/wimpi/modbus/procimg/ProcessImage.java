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
 ***/

package net.wimpi.modbus.procimg;

/**
 * Interface defining a process image in an object oriented manner.
 * <p>
 * The process image is understood as a shared memory area used for
 * communication between a local slave and a remote master or device. The
 * process image is the point of contact between a slave application and the
 * jamod library. When a listener receives a request, it will use the configured
 * process image to try and fulfil the request. That is, for read requests it
 * will attempt to 'get' the corresponding address. For write requests it will
 * create get the corresponding address and then update the value.
 * <p>
 * The functions for interacting with the process image also include the unit ID
 * included with the request. Process image implementations are free to ignore
 * this argument, although if multiple slaves are to be installed on the same
 * network, an implementation will likely have to respect the unit ID of the
 * request.
 * <p>
 * The process image can also optionally be used by master applications.  The
 * process image can specify a non-default ProcessImageFactory.  Master 
 * applications can use this ProcessImageFactory to control the types of instances
 * returned in ReadInputRegistersResponse and ReadMultipleRegistersResponse.
 * <p>
 * Implementations are encouraged to follow the following principles:
 * <ul>
 * <li>If a request is made to an invalid offset, then the implementation should
 * throw an IllegalAddressException. If a request is made for multiple values,
 * and some of them are valid but some are not, then the implementation should
 * again throw an IllegalAddressException; it should not attempt to partially
 * fulfil the request. When an {@link IllegalAddressException} is thrown, the
 * listener will generate an illegal address Modbus message to send back to the
 * master.</li>
 * <li>If a request is made with a unit ID that doesn't correspond to the slave
 * (or one of the slaves) that the process image represents, then the function
 * should throw an {@link InvalidUnitIDException}. This signifies to the
 * listener that no response should be sent. If a node receives a request
 * destined for another node, then it should be ignored.</li>
 * </ul>
 * 
 * @author Dieter Wimberger
 * @author Charles Hache
 * @version @version@ (@date@)
 */
public interface ProcessImage {

	/**
	 * Gets a ProcessImageFactory for this ProcessImage. The ProcessImageFactory
	 * returned by this function is used by the modbus slave when a master
	 * attempts to write to it. The slave creates instances of registers or
	 * coils, then calls this ProcessImage's set functions.
	 * <p>
	 * The factory is also used by masters when creating instances to return in
	 * ReadInputRegistersResponse and ReadMultipleRegistersResponse instances.
	 * 
	 * @return A ProcessImageFactory that creates instances for this
	 *         ProcessImage.
	 */
	public ProcessImageFactory getProcessImageFactory();

	/**
	 * Returns a range of <tt>DigitalOut</tt> instances.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param offset
	 *            the start offset.
	 * @param count
	 *            the amount of <tt>DigitalOut</tt> from the offset.
	 * 
	 * @return an array of <tt>DigitalOut</tt> instances.
	 * 
	 * @throws IllegalAddressException
	 *             if the range from offset to offset+count is non existent. An
	 *             error modbus message is sent back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public DigitalOut[] getDigitalOutRange(int unitId, int offset, int count)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns the <tt>DigitalOut</tt> instance at the given reference.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param ref
	 *            the reference.
	 * 
	 * @return the <tt>DigitalOut</tt> instance at the given address.
	 * 
	 * @throws IllegalAddressException
	 *             if the reference is invalid. An error modbus message is sent
	 *             back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public DigitalOut getDigitalOut(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns a range of <tt>DigitalIn</tt> instances.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param offset
	 *            the start offset.
	 * @param count
	 *            the amount of <tt>DigitalIn</tt> from the offset.
	 * 
	 * @return an array of <tt>DigitalIn</tt> instances.
	 * 
	 * @throws IllegalAddressException
	 *             if the range from offset to offset+count is non existent. An
	 *             error modbus message is sent back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public DigitalIn[] getDigitalInRange(int unitId, int offset, int count)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns the <tt>DigitalIn</tt> instance at the given reference.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param ref
	 *            the reference.
	 * 
	 * @return the <tt>DigitalIn</tt> instance at the given address.
	 * 
	 * @throws IllegalAddressException
	 *             if the reference is invalid. An error modbus message is sent
	 *             back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public DigitalIn getDigitalIn(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns a range of <tt>InputRegister</tt> instances.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param offset
	 *            the start offset.
	 * @param count
	 *            the amount of <tt>InputRegister</tt> from the offset.
	 * 
	 * @return an array of <tt>InputRegister</tt> instances.
	 * 
	 * @throws IllegalAddressException
	 *             if the range from offset to offset+count is non existent. An
	 *             error modbus message is sent back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public InputRegister[] getInputRegisterRange(int unitId, int offset,
			int count) throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns the <tt>InputRegister</tt> instance at the given reference.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param ref
	 *            the reference.
	 * 
	 * @return the <tt>InputRegister</tt> instance at the given address.
	 * 
	 * @throws IllegalAddressException
	 *             if the reference is invalid. An error modbus message is sent
	 *             back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public InputRegister getInputRegister(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns a range of <tt>Register</tt> instances.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param offset
	 *            the start offset.
	 * @param count
	 *            the amount of <tt>Register</tt> from the offset.
	 * 
	 * @return an array of <tt>Register</tt> instances.
	 * 
	 * @throws IllegalAddressException
	 *             if the range from offset to offset+count is non existent. An
	 *             error modbus message is sent back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public Register[] getRegisterRange(int unitId, int offset, int count)
			throws IllegalAddressException, InvalidUnitIDException;

	/**
	 * Returns the <tt>Register</tt> instance at the given reference.
	 * <p>
	 * 
	 * @param unitId
	 *            The unitID from the request
	 * @param ref
	 *            the reference.
	 * 
	 * @return the <tt>Register</tt> instance at the given address.
	 * 
	 * @throws IllegalAddressException
	 *             if the reference is invalid. An error modbus message is sent
	 *             back to the master.
	 * @throws InvalidUnitIDException
	 *             if the unit ID is invalid for this process image. No reply is
	 *             sent back to the master.
	 */
	public Register getRegister(int unitId, int ref)
			throws IllegalAddressException, InvalidUnitIDException;
	
}// interface ProcessImage
