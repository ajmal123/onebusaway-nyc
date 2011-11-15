/*
 * Copyright (c) 2011 Metropolitan Transportation Authority
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

var OBA = window.OBA || {};

/*
 * This is mostly for IE7, to make sure AJAX/JSON calls are not cached. 
 */
$.ajaxSetup({ cache: false });

OBA.Config = {		
		searchUrl: "api/search",
		configUrl: "api/config",

		siriSMUrl: "api/siri/stop-monitoring",
		siriVMUrl: "api/siri/vehicle-monitoring",
		
		refreshInterval: 15000,
		
		googleAnalyticsId: 'UA-XXXXXXXX-X',

		analyticsFunction: function(type, value) {
			_gaq.push(['_trackEvent', "Desktop Web", type, value]);
		}
};
