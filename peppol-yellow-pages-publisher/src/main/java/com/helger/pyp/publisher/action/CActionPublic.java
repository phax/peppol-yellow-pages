/**
 * Copyright (C) 2015 Philip Helger (www.helger.com)
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
package com.helger.pyp.publisher.action;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.photon.core.action.IActionDeclaration;
import com.helger.photon.core.action.IActionInvoker;
import com.helger.photon.core.action.decl.PublicApplicationActionDeclaration;
import com.helger.photon.uictrls.datatables.ajax.ActionExecutorDataTablesI18N;
import com.helger.pyp.publisher.app.AppCommonUI;

/**
 * This class defines the available actions for the view app
 *
 * @author Philip Helger
 */
@Immutable
public final class CActionPublic
{
  public static final IActionDeclaration DATATABLES_I18N = new PublicApplicationActionDeclaration ("datatables-i18n",
                                                                                                   new ActionExecutorDataTablesI18N (AppCommonUI.DEFAULT_LOCALE));

  private CActionPublic ()
  {}

  public static void initActions (@Nonnull final IActionInvoker aActionInvoker)
  {
    aActionInvoker.registerAction (DATATABLES_I18N);
  }
}
