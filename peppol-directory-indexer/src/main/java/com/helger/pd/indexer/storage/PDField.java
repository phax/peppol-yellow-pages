/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
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
package com.helger.pd.indexer.storage;

import java.time.LocalDateTime;

import org.apache.lucene.document.Field;

import com.helger.commons.datetime.PDTFactory;
import com.helger.pd.indexer.storage.field.PDNumericField;
import com.helger.pd.indexer.storage.field.PDStringField;
import com.helger.pd.indexer.storage.field.PDStringField.EPDTokenize;
import com.helger.peppol.identifier.generic.doctype.IDocumentTypeIdentifier;
import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;

public class PDField
{
  public static final PDStringField <IParticipantIdentifier> PARTICIPANT_ID = PDStringField.createParticipantIdentifier ("participantid",
                                                                                                                         Field.Store.YES,
                                                                                                                         EPDTokenize.NO_TOKENIZE);
  public static final PDStringField <IDocumentTypeIdentifier> DOCTYPE_ID = PDStringField.createDocumentTypeIdentifier ("doctypeid",
                                                                                                                       Field.Store.YES,
                                                                                                                       EPDTokenize.NO_TOKENIZE);
  public static final PDStringField <String> REGISTRATION_DATE = PDStringField.createString ("registrationdate",
                                                                                             Field.Store.YES,
                                                                                             EPDTokenize.NO_TOKENIZE);
  public static final PDStringField <String> NAME = PDStringField.createString ("name",
                                                                                Field.Store.YES,
                                                                                EPDTokenize.TOKENIZE);
  public static final PDStringField <String> COUNTRY_CODE = PDStringField.createString ("country",
                                                                                        Field.Store.YES,
                                                                                        EPDTokenize.NO_TOKENIZE);
  public static final PDStringField <String> GEO_INFO = PDStringField.createString ("geoinfo",
                                                                                    Field.Store.YES,
                                                                                    EPDTokenize.TOKENIZE);
  public static final PDStringField <String> IDENTIFIER_SCHEME = PDStringField.createString ("identifiertype",
                                                                                             Field.Store.YES,
                                                                                             EPDTokenize.TOKENIZE);
  public static final PDStringField <String> IDENTIFIER_VALUE = PDStringField.createString ("identifier",
                                                                                            Field.Store.YES,
                                                                                            EPDTokenize.TOKENIZE);
  public static final PDStringField <String> WEBSITE_URI = PDStringField.createString ("website",
                                                                                       Field.Store.YES,
                                                                                       EPDTokenize.TOKENIZE);
  public static final PDStringField <String> CONTACT_TYPE = PDStringField.createString ("bc-description",
                                                                                        Field.Store.YES,
                                                                                        EPDTokenize.TOKENIZE);
  public static final PDStringField <String> CONTACT_NAME = PDStringField.createString ("bc-name",
                                                                                        Field.Store.YES,
                                                                                        EPDTokenize.TOKENIZE);
  public static final PDStringField <String> CONTACT_PHONE = PDStringField.createString ("bc-phone",
                                                                                         Field.Store.YES,
                                                                                         EPDTokenize.TOKENIZE);
  public static final PDStringField <String> CONTACT_EMAIL = PDStringField.createString ("bc-email",
                                                                                         Field.Store.YES,
                                                                                         EPDTokenize.TOKENIZE);
  public static final PDStringField <String> ADDITIONAL_INFO = PDStringField.createString ("freetext",
                                                                                           Field.Store.YES,
                                                                                           EPDTokenize.TOKENIZE);

  public static final PDNumericField <LocalDateTime> METADATA_CREATIONDT = new PDNumericField<> ("md-creationdt",
                                                                                           x -> Long.valueOf (PDTFactory.getMillis (x)),
                                                                                           x -> PDTFactory.createLocalDateTime (x.longValue ()),
                                                                                           Field.Store.YES);
  public static final PDStringField <String> METADATA_OWNERID = PDStringField.createString ("md-ownerid",
                                                                                            Field.Store.YES,
                                                                                            EPDTokenize.NO_TOKENIZE);
  public static final PDStringField <String> METADATA_REQUESTING_HOST = PDStringField.createString ("md-requestinghost",
                                                                                                    Field.Store.YES,
                                                                                                    EPDTokenize.NO_TOKENIZE);

  private PDField ()
  {}
}
