/*
 * 03/21/2010
 *
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This code is licensed under the LGPL.  See the "license.txt" file included
 * with this project.
 */
package org.fife.rsta.ac.java.classreader;

import java.io.DataInputStream;
import java.io.IOException;

import org.fife.rsta.ac.java.classreader.attributes.AttributeInfo;


/**
 * Base class for information about members (fields and methods).
 *
 * @author Robert Futrell
 * @version 1.0
 * @see FieldInfo
 * @see MethodInfo
 */
public abstract class MemberInfo {

	/**
	 * The class file in which this method is defined.
	 */
	protected ClassFile cf;

	/**
	 * A mask of flags used to denote access permission to and properties of
	 * this method.
	 */
	private int accessFlags; // u2

	/**
	 * Whether this member is deprecated.
	 */
	private boolean deprecated;

	/**
	 * Attribute marking a member as deprecated.
	 */
	public static final String DEPRECATED			= "Deprecated";


	/**
	 * Constructor.
	 *
	 * @param cf The class file defining this member.
	 */
	protected MemberInfo(ClassFile cf, int accessFlags) {
		this.cf = cf;
		this.accessFlags = accessFlags;
	}


	/**
	 * Returns the access flags for this field.
	 *
	 * @return The access flags, as a bit field.
	 * @see AccessFlags
	 */
	public int getAccessFlags() {
		return accessFlags;
	}


	/**
	 * Returns the parent class file.
	 *
	 * @return The parent class file.
	 */
	public ClassFile getClassFile() {
		return cf;
	}


	/**
	 * Returns the name of this member.
	 *
	 * @return The name of this member.
	 */
	public abstract String getName();


	/**
	 * Returns whether this member is deprecated.
	 *
	 * @return Whether this member is deprecated.
	 */
	public boolean isDeprecated() {
		return deprecated;
	}


	/**
	 * Returns the descriptor of this member.
	 *
	 * @return The descriptor of this member.
	 */
	public abstract String getDescriptor();


	/**
	 * Returns whether this member is final.
	 *
	 * @return Whether this member is final.
	 */
	public boolean isFinal() {
		return (getAccessFlags()&AccessFlags.ACC_FINAL)>0;
	}


	/**
	 * Returns whether this member is static.
	 *
	 * @return Whether this member is static.
	 */
	public boolean isStatic() {
		return (getAccessFlags()&AccessFlags.ACC_STATIC)>0;
	}


	/**
	 * Reads attributes common to all members.  If the specified attribute is
	 * not common to members, the attribute returned is an "unsupported"
	 * attribute.
	 *
	 * @param in
	 * @param attrName
	 * @param attrLength
	 * @return
	 * @throws IOException
	 */
	protected AttributeInfo readAttribute(DataInputStream in, String attrName,
										int attrLength) throws IOException {

		AttributeInfo ai = null;

		if (DEPRECATED.equals(attrName)) { // 4.7.10
			// No need to read anything else, attributeLength==0
			deprecated = true;
		}

		else {
			// System.out.println("Unsupported attribute: " + attrName);
			ai = AttributeInfo.readUnsupportedAttribute(cf, in, attrName,
				attrLength);
		}

		return ai;

	}


}