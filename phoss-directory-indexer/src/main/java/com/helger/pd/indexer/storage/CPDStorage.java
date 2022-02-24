/*
 * Copyright (C) 2015-2022 Philip Helger (www.helger.com)
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

import javax.annotation.concurrent.Immutable;

/**
 * Constants Lucene field names
 *
 * @author Philip Helger
 */
@Immutable
public final class CPDStorage
{
  public static final String FIELD_ALL_FIELDS = "allfields";
  @Deprecated
  public static final String FIELD_DELETED = "deleted";

  private CPDStorage ()
  {}
}
