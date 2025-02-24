/*
 * Copyright (C) 2015-2025 Philip Helger (www.helger.com)
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
package com.helger.pd.indexer.lucene;

import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.lucene.document.Document;

/**
 * {@link Document} retrieval interface
 *
 * @author Philip Helger
 */
@FunctionalInterface
public interface ILuceneDocumentProvider
{
  /**
   * Get the Lucene document from the document ID
   *
   * @param nDocID
   *        Internal Lucene Document ID
   * @return The Document or <code>null</code>.
   * @throws IOException
   *         In case of a Lucene error
   */
  @Nullable
  Document getDocument (int nDocID) throws IOException;
}
