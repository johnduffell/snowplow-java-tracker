/*
 * Copyright (c) 2014 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package com.snowplowanalytics.snowplow.tracker;

import java.util.Map;

public class Tracker {

    private boolean base64Encoded = true;
    private Emitter emitter;
    private String appId;
    private String namespace;

    public Tracker(Emitter emitter, String appId, boolean base64Encoded, String namespace) {
        this.emitter = emitter;
        this.appId = appId;
        this.base64Encoded = base64Encoded;
        this.namespace = namespace;
    }

    private Payload completePayload(Payload payload, double timestamp) {
        payload.add(Parameter.TID, Util.getTransactionId());
        payload.add(Parameter.TIMESTAMP, Util.getTimestamp());

        payload.add(Parameter.TRACKER_VERSION, Version.VERSION);

        // If timestamp is set to 0, generate one
        payload.add(Parameter.TIMESTAMP, (timestamp == 0 ? Util.getTimestamp() : timestamp));

        return payload;
    }

    private void addTrackerPayload(Payload payload) {
        this.emitter.addToBuffer(payload);
    }

    public void trackPageView(String pageUrl, String pageTitle, String referrer, Map context) {
        trackPageView(pageUrl,pageTitle, referrer,context, 0);
    }

    public void trackPageView(String pageUrl, String pageTitle, String referrer,
                              Map context, double timestamp) {
        Payload payload = new TrackerPayload();
        payload.add(Parameter.EVENT, Constants.EVENT_PAGE_VIEW);
        payload.add(Parameter.PAGE_URL, pageUrl);
        payload.add(Parameter.PAGE_TITLE, pageTitle);
        payload.add(Parameter.PAGE_REFR, referrer);

        completePayload(payload, timestamp);

        addTrackerPayload(payload);
    }
}
