/**
 * Copyright (C) 2015-2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.helger.pd.businesscard.generic;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.lang.ICloneable;
import com.helger.commons.string.ToStringGenerator;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroElement;

/**
 * Generic contact.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class PDContact implements Serializable, ICloneable <PDContact>
{
  private String m_sType;
  private String m_sName;
  private String m_sPhoneNumber;
  private String m_sEmail;

  public PDContact ()
  {}

  public PDContact (@Nullable final String sType,
                    @Nullable final String sName,
                    @Nullable final String sPhoneNumber,
                    @Nullable final String sEmail)
  {
    setType (sType);
    setName (sName);
    setPhoneNumber (sPhoneNumber);
    setEmail (sEmail);
  }

  /**
   * Gets the value of the type property.
   *
   * @return possible object is {@link String }
   */
  @Nullable
  public String getType ()
  {
    return m_sType;
  }

  /**
   * Sets the value of the type property.
   *
   * @param sType
   *        allowed object is {@link String }
   */
  public final void setType (@Nullable final String sType)
  {
    m_sType = sType;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is {@link String }
   */
  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  /**
   * Sets the value of the name property.
   *
   * @param sName
   *        allowed object is {@link String }
   */
  public final void setName (@Nullable final String sName)
  {
    m_sName = sName;
  }

  /**
   * Gets the value of the phoneNumber property.
   *
   * @return possible object is {@link String }
   */
  @Nullable
  public String getPhoneNumber ()
  {
    return m_sPhoneNumber;
  }

  /**
   * Sets the value of the phoneNumber property.
   *
   * @param sPhoneNumber
   *        allowed object is {@link String }
   */
  public final void setPhoneNumber (@Nullable final String sPhoneNumber)
  {
    m_sPhoneNumber = sPhoneNumber;
  }

  /**
   * Gets the value of the email property.
   *
   * @return possible object is {@link String }
   */
  @Nullable
  public String getEmail ()
  {
    return m_sEmail;
  }

  /**
   * Sets the value of the email property.
   *
   * @param sEmail
   *        allowed object is {@link String }
   */
  public final void setEmail (@Nullable final String sEmail)
  {
    m_sEmail = sEmail;
  }

  /**
   * This method clones all values from <code>this</code> to the passed object.
   * All data in the parameter object is overwritten!
   *
   * @param ret
   *        The target object to clone to. May not be <code>null</code>.
   */
  public void cloneTo (@Nonnull final PDContact ret)
  {
    ret.m_sEmail = m_sEmail;
    ret.m_sName = m_sName;
    ret.m_sPhoneNumber = m_sPhoneNumber;
    ret.m_sType = m_sType;
  }

  @Nonnull
  @ReturnsMutableCopy
  public PDContact getClone ()
  {
    final PDContact ret = new PDContact ();
    cloneTo (ret);
    return ret;
  }

  @Nonnull
  public IMicroElement getAsMicroXML (@Nullable final String sNamespaceURI,
                                      @Nonnull @Nonempty final String sElementName)
  {
    final IMicroElement ret = new MicroElement (sNamespaceURI, sElementName);
    ret.setAttribute ("email", m_sEmail);
    ret.setAttribute ("name", m_sName);
    ret.setAttribute ("phonenumber", m_sPhoneNumber);
    ret.setAttribute ("type", m_sType);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;

    final PDContact rhs = (PDContact) o;
    return EqualsHelper.equals (m_sType, rhs.m_sType) &&
           EqualsHelper.equals (m_sName, rhs.m_sName) &&
           EqualsHelper.equals (m_sPhoneNumber, rhs.m_sPhoneNumber) &&
           EqualsHelper.equals (m_sEmail, rhs.m_sEmail);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sType)
                                       .append (m_sName)
                                       .append (m_sPhoneNumber)
                                       .append (m_sEmail)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("type", m_sType)
                                       .append ("name", m_sName)
                                       .append ("phoneNumber", m_sPhoneNumber)
                                       .append ("email", m_sEmail)
                                       .getToString ();
  }
}
