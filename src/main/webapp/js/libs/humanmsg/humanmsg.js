/*
 * IDCopier
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*
	HUMANIZED MESSAGES 1.0
	idea - http://www.humanized.com/weblog/2006/09/11/monolog_boxes_and_transparent_messages
	home - http://humanmsg.googlecode.com
*/

var humanMsg = {
	setup: function(appendTo, logName, msgOpacity) {
		humanMsg.msgID = 'humanMsg';
		humanMsg.logID = 'humanMsgLog';

		// appendTo is the element the msg is appended to
		if (appendTo == undefined)
			appendTo = 'body';

		// The text on the Log tab
		if (logName == undefined)
			logName = 'Message Log';

		// Opacity of the message
		humanMsg.msgOpacity = .8;

		if (msgOpacity != undefined) 
			humanMsg.msgOpacity = parseFloat(msgOpacity);

		// Inject the message structure
		jQuery(appendTo).append('<div id="'+humanMsg.msgID+'" class="humanMsg"><div class="round"></div><p></p><div class="round"></div></div> <div id="'+humanMsg.logID+'"><p>'+logName+'</p><ul></ul></div>')
		
		jQuery('#'+humanMsg.logID+' p').click(
			function() { jQuery(this).siblings('ul').slideToggle() }
		)
	},

	displayMsg: function(msg) {
		if (msg == '')
			return;

		clearTimeout(humanMsg.t2);

		// Inject message
		jQuery('#'+humanMsg.msgID+' p').html(msg)
	
		// Show message
		jQuery('#'+humanMsg.msgID+'').show().animate({ opacity: humanMsg.msgOpacity}, 200, function() {
			jQuery('#'+humanMsg.logID)
				.show().children('ul').prepend('<li>'+msg+'</li>')	// Prepend message to log
				.children('li:first').slideDown(200)				// Slide it down
		
			if ( jQuery('#'+humanMsg.logID+' ul').css('display') == 'none') {
				jQuery('#'+humanMsg.logID+' p').animate({ bottom: 40 }, 200, 'linear', function() {
					jQuery(this).animate({ bottom: 0 }, 300, 'easeOutBounce', function() { jQuery(this).css({ bottom: 0 }) })
				})
			}
			
		})

		// Watch for mouse & keyboard in .5s
		humanMsg.t1 = setTimeout("humanMsg.bindEvents()", 700)
		// Remove message after 5s
		humanMsg.t2 = setTimeout("humanMsg.removeMsg()", 5000)
	},

	bindEvents: function() {
	// Remove message if mouse is moved or key is pressed
		jQuery(window)
			.mousemove(humanMsg.removeMsg)
			.click(humanMsg.removeMsg)
			.keypress(humanMsg.removeMsg)
	},

	removeMsg: function() {
		// Unbind mouse & keyboard
		jQuery(window)
			.unbind('mousemove', humanMsg.removeMsg)
			.unbind('click', humanMsg.removeMsg)
			.unbind('keypress', humanMsg.removeMsg)

		// If message is fully transparent, fade it out
		if (jQuery('#'+humanMsg.msgID).css('opacity') == humanMsg.msgOpacity)
			jQuery('#'+humanMsg.msgID).animate({ opacity: 0 }, 500, function() { jQuery(this).hide() })
	}
};

jQuery(document).ready(function(){
	humanMsg.setup();
})