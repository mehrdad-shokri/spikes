/**
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.cognito.sync.devauth.client;

import java.io.IOException;

import okhttp3.Response;

/**
 * This class is used to parse the response of the GetToken call of the sample
 * Cognito developer authentication and store it as a GetTokenResponse object.
 */
public class GetTokenResponseHandler extends ResponseHandler {

    private final String key;

    public GetTokenResponseHandler(final String key) {
        this.key = key;
    }

    @Override
    public ResponseData handleResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            try {
                String json = AESEncryption.unwrap(response.body().string(), this.key);
                String identityId = Utilities.extractElement(json, "identityId");
                String identityPoolId = Utilities.extractElement(json, "identityPoolId");
                String token = Utilities.extractElement(json, "token");
                return new GetTokenResponseData(identityId, identityPoolId, token);
            } catch (Exception exception) {
                throw new IOException(exception);
            }
        } else {
            return new GetTokenResponseData(response.code(), response.body().string());
        }
    }

}
