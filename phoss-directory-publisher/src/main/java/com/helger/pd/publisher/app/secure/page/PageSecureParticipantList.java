/**
 * Copyright (C) 2015-2019 Philip Helger (www.helger.com)
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
package com.helger.pd.publisher.app.secure.page;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.impl.ICommonsSortedMap;
import com.helger.commons.mutable.MutableInt;
import com.helger.commons.url.ISimpleURL;
import com.helger.html.hc.html.sections.HCH3;
import com.helger.html.hc.html.tabular.HCRow;
import com.helger.html.hc.html.tabular.HCTable;
import com.helger.html.hc.html.tabular.IHCCell;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.pd.indexer.mgr.PDMetaManager;
import com.helger.pd.indexer.storage.EQueryMode;
import com.helger.pd.publisher.app.pub.CMenuPublic;
import com.helger.pd.publisher.app.pub.page.PagePublicSearchSimple;
import com.helger.pd.publisher.app.secure.CMenuSecure;
import com.helger.pd.publisher.ui.AbstractAppWebPage;
import com.helger.peppolid.IParticipantIdentifier;
import com.helger.peppolid.factory.IIdentifierFactory;
import com.helger.photon.bootstrap4.alert.BootstrapErrorBox;
import com.helger.photon.bootstrap4.alert.BootstrapInfoBox;
import com.helger.photon.bootstrap4.uictrls.datatables.BootstrapDTColAction;
import com.helger.photon.bootstrap4.uictrls.datatables.BootstrapDataTables;
import com.helger.photon.core.appid.CApplicationID;
import com.helger.photon.uicore.css.CPageParam;
import com.helger.photon.uicore.page.WebPageExecutionContext;
import com.helger.photon.uictrls.datatables.column.DTCol;
import com.helger.photon.uictrls.datatables.column.EDTColType;
import com.helger.web.scope.IRequestWebScopeWithoutResponse;

public final class PageSecureParticipantList extends AbstractAppWebPage
{
  private static final String FIELD_PARTICIPANT_ID = "partid";

  public PageSecureParticipantList (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Participant list");
  }

  @Override
  protected void fillContent (final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final IRequestWebScopeWithoutResponse aRequestScope = aWPEC.getRequestScope ();

    if (aWPEC.hasAction (CPageParam.ACTION_DELETE))
    {
      final String sParticipantID = aRequestScope.params ().getAsString (FIELD_PARTICIPANT_ID);

      final IIdentifierFactory aIdentifierFactory = PDMetaManager.getIdentifierFactory ();
      final IParticipantIdentifier aParticipantID = aIdentifierFactory.parseParticipantIdentifier (sParticipantID);

      if (aParticipantID != null)
      {
        boolean bSuccess = false;
        try
        {
          bSuccess = PDMetaManager.getStorageMgr ().deleteEntry (aParticipantID, null).isSuccess ();
        }
        catch (final IOException ex)
        {
          // ignore
        }
        if (bSuccess)
          aNodeList.addChild (new BootstrapInfoBox ().addChild ("The participant '" +
                                                                aParticipantID.getURIEncoded () +
                                                                "' was scheduled for deletion"));
        else
          aNodeList.addChild (new BootstrapErrorBox ().addChild ("Error scheduling participant '" +
                                                                 aParticipantID.getURIEncoded () +
                                                                 "' for deletion"));
      }
    }

    final ICommonsSortedMap <IParticipantIdentifier, MutableInt> aAllIDs = PDMetaManager.getStorageMgr ()
                                                                                        .getAllContainedParticipantIDs (EQueryMode.NON_DELETED_ONLY);
    aNodeList.addChild (new HCH3 ().addChild (aAllIDs.size () + " participants (=Business Cards) are contained"));

    final HCTable aTable = new HCTable (new DTCol ("ID"),
                                        new DTCol ("Entities").setDisplayType (EDTColType.INT, aDisplayLocale),
                                        new BootstrapDTColAction ()).setID (getID ());
    for (final Map.Entry <IParticipantIdentifier, MutableInt> aEntry : aAllIDs.entrySet ())
    {
      final String sParticipantID = aEntry.getKey ().getURIEncoded ();

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (sParticipantID);
      aRow.addCell (Integer.toString (aEntry.getValue ().intValue ()));

      final IHCCell <?> aActionCell = aRow.addCell ();
      final ISimpleURL aShowDetails = aWPEC.getLinkToMenuItem (CApplicationID.APP_ID_PUBLIC,
                                                               CMenuPublic.MENU_SEARCH_SIMPLE)
                                           .add (PagePublicSearchSimple.FIELD_QUERY, sParticipantID)
                                           .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_VIEW)
                                           .add (PagePublicSearchSimple.FIELD_PARTICIPANT_ID, sParticipantID);
      aActionCell.addChild (new HCA (aShowDetails).addChild ("Search"));
      aActionCell.addChild (" ");
      final ISimpleURL aReIndex = aWPEC.getLinkToMenuItem (CMenuSecure.MENU_INDEX_MANUALLY)
                                       .add (PageSecureIndexManually.FIELD_PARTICIPANT_ID, sParticipantID)
                                       .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_PERFORM);
      aActionCell.addChild (new HCA (aReIndex).addChild ("Reindex"));
      aActionCell.addChild (" ");
      final ISimpleURL aDelete = aWPEC.getSelfHref ()
                                      .add (FIELD_PARTICIPANT_ID, sParticipantID)
                                      .add (CPageParam.PARAM_ACTION, CPageParam.ACTION_DELETE);
      aActionCell.addChild (new HCA (aDelete).addChild ("Delete"));
    }

    aNodeList.addChild (aTable).addChild (BootstrapDataTables.createDefaultDataTables (aWPEC, aTable));
  }
}
