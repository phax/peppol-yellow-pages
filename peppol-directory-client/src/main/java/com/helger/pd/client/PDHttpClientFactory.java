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
package com.helger.pd.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.annotation.Nullable;
import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ws.HostnameVerifierVerifyAll;
import com.helger.httpclient.HttpClientFactory;
import com.helger.peppol.utils.PeppolKeyStoreHelper;
import com.helger.security.keystore.KeyStoreHelper;
import com.helger.security.keystore.LoadedKeyStore;

public final class PDHttpClientFactory extends HttpClientFactory
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PDHttpClientFactory.class);

  public PDHttpClientFactory ()
  {
    setHostnameVerifier (new HostnameVerifierVerifyAll (false));
  }

  @Override
  @Nullable
  public SSLContext createSSLContext () throws KeyManagementException,
                                        UnrecoverableKeyException,
                                        NoSuchAlgorithmException,
                                        KeyStoreException
  {
    // Load key store
    final LoadedKeyStore aLoadedKeyStore = KeyStoreHelper.loadKeyStore (PDClientConfiguration.getKeyStorePath (),
                                                                        PDClientConfiguration.getKeyStorePassword ());
    if (aLoadedKeyStore.isFailure ())
    {
      s_aLogger.error ("Failed to initialize keystore for service connection! Can only use http now! Details: " +
                       PeppolKeyStoreHelper.getLoadError (aLoadedKeyStore));
      return null;
    }
    return SSLContexts.custom ()
                      .loadKeyMaterial (aLoadedKeyStore.getKeyStore (),
                                        PDClientConfiguration.getKeyStoreKeyPassword (),
                                        (aAliases, aSocket) -> {
                                          final String sAlias = PDClientConfiguration.getKeyStoreKeyAlias ();
                                          return aAliases.containsKey (sAlias) ? sAlias : null;
                                        })
                      .build ();
  }
}
