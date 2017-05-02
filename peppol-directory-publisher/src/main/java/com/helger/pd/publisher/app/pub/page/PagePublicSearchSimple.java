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
package com.helger.pd.publisher.app.pub.page;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.lucene.search.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.collection.multimap.IMultiMapListBased;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.locale.country.CountryCache;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.SimpleURL;
import com.helger.html.css.DefaultCSSClassProvider;
import com.helger.html.css.ICSSClassProvider;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.ext.HCExtHelper;
import com.helger.html.hc.html.forms.EHCFormMethod;
import com.helger.html.hc.html.forms.HCEdit;
import com.helger.html.hc.html.forms.HCForm;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.html.grouping.HCOL;
import com.helger.html.hc.html.grouping.HCUL;
import com.helger.html.hc.html.grouping.IHCLI;
import com.helger.html.hc.html.sections.HCH1;
import com.helger.html.hc.html.tabular.HCCol;
import com.helger.html.hc.html.textlevel.HCCode;
import com.helger.html.hc.html.textlevel.HCSmall;
import com.helger.html.hc.html.textlevel.HCSpan;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.pd.indexer.mgr.PDMetaManager;
import com.helger.pd.indexer.storage.PDQueryManager;
import com.helger.pd.indexer.storage.PDStorageManager;
import com.helger.pd.indexer.storage.PDStoredDocument;
import com.helger.pd.publisher.app.AppCommonUI;
import com.helger.pd.publisher.ui.AbstractAppWebPage;
import com.helger.pd.publisher.ui.HCExtImg;
import com.helger.pd.publisher.ui.PDCommonUI;
import com.helger.peppol.identifier.factory.IIdentifierFactory;
import com.helger.peppol.identifier.factory.PeppolIdentifierFactory;
import com.helger.peppol.identifier.generic.doctype.IDocumentTypeIdentifier;
import com.helger.peppol.identifier.generic.participant.IParticipantIdentifier;
import com.helger.peppol.identifier.peppol.PeppolIdentifierHelper;
import com.helger.peppol.identifier.peppol.doctype.IPeppolDocumentTypeIdentifierParts;
import com.helger.peppol.identifier.peppol.issuingagency.IIdentifierIssuingAgency;
import com.helger.photon.bootstrap3.CBootstrapCSS;
import com.helger.photon.bootstrap3.alert.BootstrapErrorBox;
import com.helger.photon.bootstrap3.alert.BootstrapInfoBox;
import com.helger.photon.bootstrap3.alert.BootstrapWarnBox;
import com.helger.photon.bootstrap3.badge.BootstrapBadge;
import com.helger.photon.bootstrap3.button.BootstrapButton;
import com.helger.photon.bootstrap3.button.BootstrapSubmitButton;
import com.helger.photon.bootstrap3.button.EBootstrapButtonSize;
import com.helger.photon.bootstrap3.button.EBootstrapButtonType;
import com.helger.photon.bootstrap3.form.BootstrapFormGroup;
import com.helger.photon.bootstrap3.form.BootstrapViewForm;
import com.helger.photon.bootstrap3.grid.BootstrapRow;
import com.helger.photon.bootstrap3.inputgroup.BootstrapInputGroup;
import com.helger.photon.bootstrap3.label.BootstrapLabel;
import com.helger.photon.bootstrap3.label.EBootstrapLabelType;
import com.helger.photon.bootstrap3.nav.BootstrapTabBox;
import com.helger.photon.bootstrap3.pageheader.BootstrapPageHeader;
import com.helger.photon.bootstrap3.panel.BootstrapPanel;
import com.helger.photon.bootstrap3.table.BootstrapTable;
import com.helger.photon.core.form.RequestField;
import com.helger.photon.uicore.css.CPageParam;
import com.helger.photon.uicore.icon.EDefaultIcon;
import com.helger.photon.uicore.page.WebPageExecutionContext;

public final class PagePublicSearchSimple extends AbstractAppWebPage
{
  public static final String FIELD_QUERY = "q";
  public static final String FIELD_PARTICIPANT_ID = "partid";
  public static final String PARAM_MAX = "max";
  private static final Logger s_aLogger = LoggerFactory.getLogger (PagePublicSearchSimple.class);

  private static final ICSSClassProvider CSS_CLASS_BIG_QUERY_BOX = DefaultCSSClassProvider.create ("big-querybox");
  private static final ICSSClassProvider CSS_CLASS_BIG_QUERY_HELPTEXT = DefaultCSSClassProvider.create ("big-queryhelptext");
  private static final ICSSClassProvider CSS_CLASS_BIG_QUERY_BUTTONS = DefaultCSSClassProvider.create ("big-querybuttons");
  private static final ICSSClassProvider CSS_CLASS_SMALL_QUERY_BOX = DefaultCSSClassProvider.create ("small-querybox");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC = DefaultCSSClassProvider.create ("result-doc");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_HEADER = DefaultCSSClassProvider.create ("result-doc-header");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_COUNTRY_CODE = DefaultCSSClassProvider.create ("result-doc-country-code");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_NAME = DefaultCSSClassProvider.create ("result-doc-name");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_GEOINFO = DefaultCSSClassProvider.create ("result-doc-geoinfo");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_FREETEXT = DefaultCSSClassProvider.create ("result-doc-freetext");
  private static final ICSSClassProvider CSS_CLASS_RESULT_DOC_SDBUTTON = DefaultCSSClassProvider.create ("result-doc-sdbutton");
  private static final ICSSClassProvider CSS_CLASS_RESULT_PANEL = DefaultCSSClassProvider.create ("result-panel");

  public PagePublicSearchSimple (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Search");
  }

  @Override
  @Nullable
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return null;
  }

  @Nonnull
  private static HCEdit _createQueryEdit ()
  {
    return new HCEdit (new RequestField (FIELD_QUERY)).setPlaceholder ("Query PEPPOL Directory");
  }

  @Nonnull
  private BootstrapRow _createSmallQueryBox (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCForm aSmallQueryBox = new HCForm ().setAction (aWPEC.getSelfHref ()).setMethod (EHCFormMethod.GET);
    aSmallQueryBox.addChild (new BootstrapInputGroup (_createQueryEdit ()).addSuffix (new BootstrapSubmitButton ().setIcon (EDefaultIcon.MAGNIFIER))
                                                                          .addClass (CSS_CLASS_SMALL_QUERY_BOX));

    final BootstrapRow aBodyRow = new BootstrapRow ();
    aBodyRow.createColumn (12, 6, 6, 6).addChild (aSmallQueryBox);
    return aBodyRow;
  }

  @Nonnull
  private static BootstrapRow _createInitialSearchForm (final WebPageExecutionContext aWPEC)
  {
    final HCForm aBigQueryBox = new HCForm ().setAction (aWPEC.getSelfHref ()).setMethod (EHCFormMethod.GET);
    aBigQueryBox.addChild (new HCDiv ().addClass (CSS_CLASS_BIG_QUERY_BOX).addChild (_createQueryEdit ()));
    aBigQueryBox.addChild (new HCDiv ().addClass (CSS_CLASS_BIG_QUERY_HELPTEXT)
                                       .addChild ("Enter the name, address, ID or any other keyword of the entity you are looking for."));
    aBigQueryBox.addChild (new HCDiv ().addClass (CSS_CLASS_BIG_QUERY_BUTTONS)
                                       .addChild (new BootstrapSubmitButton ().addChild ("Search PEPPOL Directory")
                                                                              .setIcon (EDefaultIcon.MAGNIFIER)));

    final BootstrapRow aBodyRow = new BootstrapRow ();
    aBodyRow.createColumn (12, 1, 2, 3).addClass (CBootstrapCSS.HIDDEN_XS);
    aBodyRow.createColumn (12, 10, 8, 6).addChild (aBigQueryBox);
    aBodyRow.createColumn (12, 1, 2, 3).addClass (CBootstrapCSS.HIDDEN_XS);
    return aBodyRow;
  }

  @Nonnull
  private HCNodeList _createParticipantDetails (final Locale aDisplayLocale,
                                                final String sParticipantID,
                                                final IParticipantIdentifier aParticipantID)
  {
    final HCNodeList aDetails = new HCNodeList ();

    // Search document matching participant ID
    final ICommonsList <PDStoredDocument> aResultDocs = PDMetaManager.getStorageMgr ()
                                                                     .getAllDocumentsOfParticipant (aParticipantID);
    // Group by participant ID
    final IMultiMapListBased <IParticipantIdentifier, PDStoredDocument> aGroupedDocs = PDStorageManager.getGroupedByParticipantID (aResultDocs);
    if (aGroupedDocs.isEmpty ())
      s_aLogger.error ("No stored document matches participant identifier '" + sParticipantID + "'");
    else
    {
      if (aGroupedDocs.size () > 1)
        s_aLogger.warn ("Found " +
                        aGroupedDocs.size () +
                        " entries for participant identifier '" +
                        sParticipantID +
                        "' - weird");
      // Get the first one
      final ICommonsList <PDStoredDocument> aDocuments = aGroupedDocs.getFirstValue ();

      // Details header
      {
        IHCNode aDetailsHeader = null;
        final boolean bIsPeppolDefault = aParticipantID.hasScheme (PeppolIdentifierFactory.INSTANCE.getDefaultParticipantIdentifierScheme ());
        if (bIsPeppolDefault)
        {
          final IIdentifierIssuingAgency aIIA = AppCommonUI.getAgencyOfIdentifier (aParticipantID);
          if (aIIA != null)
          {
            aDetailsHeader = new BootstrapPageHeader ().addChild (new HCH1 ().addChild ("Details for: " +
                                                                                        aParticipantID.getValue ())
                                                                             .addChild (new HCSmall ().addChild (" (" +
                                                                                                                 aIIA.getSchemeAgency () +
                                                                                                                 ")")));
          }
        }
        if (aDetailsHeader == null)
        {
          // Fallback
          aDetailsHeader = getUIHandler ().createPageHeader ("Details for " + sParticipantID);
        }
        aDetails.addChild (aDetailsHeader);
      }

      final BootstrapTabBox aTabBox = new BootstrapTabBox ();

      // Business information
      {
        final HCNodeList aOL = new HCNodeList ();
        int nIndex = 1;
        for (final PDStoredDocument aStoredDoc : aDocuments)
        {
          final BootstrapPanel aPanel = aOL.addAndReturnChild (new BootstrapPanel ());
          aPanel.addClass (CSS_CLASS_RESULT_PANEL);
          if (aDocuments.size () > 1)
            aPanel.getOrCreateHeader ().addChild ("Business information entity " + nIndex);
          final BootstrapViewForm aViewForm = PDCommonUI.showBusinessInfoDetails (aStoredDoc, aDisplayLocale);
          aViewForm.addFormGroup (new BootstrapFormGroup ().setLabel ("Full PEPPOL participant ID")
                                                           .setCtrl (new HCCode ().addChild (sParticipantID)));
          aPanel.getBody ().addChild (aViewForm);
          ++nIndex;
        }
        // Add whole list or just the first item?
        final IHCNode aTabLabel = new HCSpan ().addChild ("Business information ")
                                               .addChild (new BootstrapBadge ().addChild (Integer.toString (aDocuments.size ())));
        aTabBox.addTab ("businessinfo", aTabLabel, aOL, true);
      }

      // Document types
      {
        final HCOL aDocTypeCtrl = new HCOL ();
        final ICommonsList <? extends IDocumentTypeIdentifier> aDocTypeIDs = aResultDocs.get (0)
                                                                                        .getAllDocumentTypeIDs ()
                                                                                        .getSortedInline (IDocumentTypeIdentifier.comparator ());
        for (final IDocumentTypeIdentifier aDocTypeID : aDocTypeIDs)
        {
          final IHCLI <?> aLI = aDocTypeCtrl.addItem ();
          aLI.addChild (PDCommonUI.getDocumentTypeID (aDocTypeID));
          if (false && GlobalDebug.isDebugMode ())
            try
            {
              final IPeppolDocumentTypeIdentifierParts aParts = PeppolIdentifierHelper.getDocumentTypeIdentifierParts (aDocTypeID);
              aLI.addChild (PDCommonUI.getDocumentTypeIDDetails (aParts));
            }
            catch (final IllegalArgumentException ex)
            {
              // Happens for non-PEPPOL identifiers
            }
        }
        aTabBox.addTab ("doctypes",
                        new HCSpan ().addChild ("Supported document types ")
                                     .addChild (new BootstrapBadge ().addChild (Integer.toString (aDocTypeIDs.size ()))),
                        aDocTypeCtrl.hasChildren () ? aDocTypeCtrl
                                                    : new BootstrapWarnBox ().addChild ("This participant does not support any document types!"),
                        false);
      }
      aDetails.addChild (aTabBox);
    }
    return aDetails;
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final IIdentifierFactory aIdentifierFactory = PDMetaManager.getIdentifierFactory ();

    {
      final BootstrapRow aHeaderRow = aNodeList.addAndReturnChild (new BootstrapRow ());
      // The logo
      aHeaderRow.createColumn (12, 12, 1, 2).addClass (CBootstrapCSS.HIDDEN_SM);
      aHeaderRow.createColumn (12, 6, 5, 4)
                .addChild (new HCExtImg (new SimpleURL ("/imgs/pd-logo.png")).addClass (CBootstrapCSS.PULL_LEFT));
      aHeaderRow.createColumn (12, 6, 5, 4)
                .addChild (new HCExtImg (new SimpleURL ("/imgs/peppol.png")).addClass (CBootstrapCSS.PULL_RIGHT));
      aHeaderRow.createColumn (12, 12, 1, 2).addClass (CBootstrapCSS.HIDDEN_SM);
    }

    final String sQuery = aWPEC.getAttributeAsString (FIELD_QUERY);
    final String sParticipantID = aWPEC.getAttributeAsString (FIELD_PARTICIPANT_ID);
    final int nMaxResults = aWPEC.getAttributeAsInt (PARAM_MAX, 50);
    boolean bShowQuery = true;

    if (aWPEC.hasAction (CPageParam.ACTION_VIEW) && StringHelper.hasText (sParticipantID))
    {
      final IParticipantIdentifier aParticipantID = aIdentifierFactory.parseParticipantIdentifier (sParticipantID);
      if (aParticipantID != null)
      {
        // Show small query box
        aNodeList.addChild (_createSmallQueryBox (aWPEC));

        final HCNodeList aDetails = _createParticipantDetails (aDisplayLocale, sParticipantID, aParticipantID);
        aNodeList.addChild (aDetails);
        bShowQuery = aDetails.hasNoChildren ();
      }
      else
        aNodeList.addChild (new BootstrapErrorBox ().addChild ("Failed to parse participant identifier '" +
                                                               sParticipantID +
                                                               "'"));
    }

    if (bShowQuery)
    {
      if (StringHelper.hasText (sQuery))
      {
        // Show small query box
        aNodeList.addChild (_createSmallQueryBox (aWPEC));

        s_aLogger.info ("Searching for '" + sQuery + "'");

        // Build Lucene query
        final Query aLuceneQuery = PDQueryManager.convertQueryStringToLuceneQuery (PDMetaManager.getLucene (), sQuery);

        if (s_aLogger.isDebugEnabled ())
          s_aLogger.debug ("Created query for '" + sQuery + "' is <" + aLuceneQuery + ">");

        // Search all documents
        final ICommonsList <PDStoredDocument> aResultDocs = PDMetaManager.getStorageMgr ()
                                                                         .getAllDocuments (aLuceneQuery);

        s_aLogger.info ("  Result for <" +
                        aLuceneQuery +
                        "> " +
                        (aResultDocs.size () == 1 ? "is 1 document" : "are " + aResultDocs.size () + " documents"));

        // Group by participant ID
        final IMultiMapListBased <IParticipantIdentifier, PDStoredDocument> aGroupedDocs = PDStorageManager.getGroupedByParticipantID (aResultDocs);

        // Display results
        if (aGroupedDocs.isEmpty ())
        {
          aNodeList.addChild (new BootstrapInfoBox ().addChild ("No search results found for query '" + sQuery + "'"));
        }
        else
        {
          aNodeList.addChild (new HCDiv ().addChild (new BootstrapLabel (EBootstrapLabelType.SUCCESS).addChild ("Found " +
                                                                                                                aGroupedDocs.size () +
                                                                                                                " entities matching '" +
                                                                                                                sQuery +
                                                                                                                "'")));
          if (aGroupedDocs.size () > nMaxResults)
            aNodeList.addChild (new HCDiv ().addChild (new BootstrapLabel (EBootstrapLabelType.WARNING).addChild ("Found many matches. Try to be nmore specific.")));

          // Show basic information
          final HCOL aOL = new HCOL ().setStart (1);
          for (final Map.Entry <IParticipantIdentifier, ICommonsList <PDStoredDocument>> aEntry : aGroupedDocs.entrySet ())
          {
            final IParticipantIdentifier aDocParticipantID = aEntry.getKey ();
            final ICommonsList <PDStoredDocument> aDocs = aEntry.getValue ();

            // Start result document
            final HCDiv aResultItem = new HCDiv ().addClass (CSS_CLASS_RESULT_DOC);
            final HCDiv aHeadRow = aResultItem.addAndReturnChild (new HCDiv ());
            {
              final boolean bIsPeppolDefault = aDocParticipantID.hasScheme (PeppolIdentifierFactory.INSTANCE.getDefaultParticipantIdentifierScheme ());
              IHCNode aParticipant = null;
              if (bIsPeppolDefault)
              {
                final IIdentifierIssuingAgency aIIA = AppCommonUI.getAgencyOfIdentifier (aDocParticipantID);
                if (aIIA != null)
                  aParticipant = new HCNodeList ().addChild (aDocParticipantID.getValue () +
                                                             " (" +
                                                             aIIA.getSchemeAgency () +
                                                             ")");
              }
              if (aParticipant == null)
              {
                // Fallback
                aParticipant = new HCCode ().addChild (aDocParticipantID.getURIEncoded ());
              }
              aHeadRow.addChild ("Participant ID: ").addChild (aParticipant);
            }
            if (aDocs.size () > 1)
              aHeadRow.addChild (" (" + aDocs.size () + " entities)");

            // Show all entities of the stored document
            final HCUL aUL = aResultItem.addAndReturnChild (new HCUL ());
            for (final PDStoredDocument aStoredDoc : aEntry.getValue ())
            {
              final BootstrapTable aTable = new BootstrapTable (HCCol.perc (20), HCCol.star ());
              aTable.setCondensed (true);
              if (aStoredDoc.hasCountryCode ())
              {
                // Add country flag (if available)
                final String sCountryCode = aStoredDoc.getCountryCode ();
                final Locale aCountry = CountryCache.getInstance ().getCountry (sCountryCode);
                aTable.addBodyRow ()
                      .addCell ("Country:")
                      .addCell (new HCNodeList ().addChild (PDCommonUI.getFlagNode (sCountryCode))
                                                 .addChild (" ")
                                                 .addChild (new HCSpan ().addChild (aCountry != null ? aCountry.getDisplayCountry (aDisplayLocale) +
                                                                                                       " (" +
                                                                                                       sCountryCode +
                                                                                                       ")"
                                                                                                     : sCountryCode)
                                                                         .addClass (CSS_CLASS_RESULT_DOC_COUNTRY_CODE)));
              }
              if (aStoredDoc.hasName ())
                aTable.addBodyRow ()
                      .addCell ("Name:")
                      .addCell (new HCSpan ().addChild (aStoredDoc.getName ()).addClass (CSS_CLASS_RESULT_DOC_NAME));

              if (aStoredDoc.hasGeoInfo ())
                aTable.addBodyRow ()
                      .addCell ("Geographical information:")
                      .addCell (new HCDiv ().addChildren (HCExtHelper.nl2divList (aStoredDoc.getGeoInfo ()))
                                            .addClass (CSS_CLASS_RESULT_DOC_GEOINFO));
              if (aStoredDoc.hasAdditionalInformation ())
                aTable.addBodyRow ()
                      .addCell ("Additional information:")
                      .addCell (new HCDiv ().addChildren (HCExtHelper.nl2divList (aStoredDoc.getAdditionalInformation ()))
                                            .addClass (CSS_CLASS_RESULT_DOC_FREETEXT));
              aUL.addAndReturnItem (aTable).addClass (CSS_CLASS_RESULT_DOC_HEADER);
            }

            final BootstrapButton aShowDetailsBtn = new BootstrapButton (EBootstrapButtonType.SUCCESS,
                                                                         EBootstrapButtonSize.DEFAULT).addChild ("Show details")
                                                                                                      .setIcon (EDefaultIcon.MAGNIFIER)
                                                                                                      .addClass (CSS_CLASS_RESULT_DOC_SDBUTTON)
                                                                                                      .setOnClick (aWPEC.getSelfHref ()
                                                                                                                        .add (FIELD_QUERY,
                                                                                                                              sQuery)
                                                                                                                        .add (CPageParam.PARAM_ACTION,
                                                                                                                              CPageParam.ACTION_VIEW)
                                                                                                                        .add (FIELD_PARTICIPANT_ID,
                                                                                                                              aDocParticipantID.getURIEncoded ()));
            aResultItem.addChild (new HCDiv ().addChild (aShowDetailsBtn));
            aOL.addItem (aResultItem);

            // Is the max result limit reached?
            if (aOL.getChildCount () >= nMaxResults)
            {
              break;
            }
          }
          aNodeList.addChild (aOL);
        }
      }
      else
      {
        // Show big query box
        final BootstrapRow aBodyRow = _createInitialSearchForm (aWPEC);
        aNodeList.addChild (aBodyRow);
      }
    }
  }
}
