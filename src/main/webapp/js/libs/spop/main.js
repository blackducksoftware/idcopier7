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
var develop, hostname;

hostname = document.location.hostname;

develop = !(hostname && (hostname.indexOf('github') >= 0 || hostname.indexOf('sebastianhelzle') >= 0));

requirejs.config({
  urlArgs: develop ? 'bust=' + (new Date()).getTime() : '',
  paths: {
    jquery: "contrib/jquery.min",
    prettify: "contrib/prettify",
    smallipop: "jquery.smallipop",
    piwik: "https://tracking.sebastianhelzle.net/piwik"
  }
});

requirejs(['jquery', 'prettify', 'piwik', 'smallipop'], function($) {
  var currentSideMenuItem, lastSideMenuY, piwikTracker, sideMenu, sideMenuItems, sideMenuMinY;
  sideMenu = $('.side-menu');
  sideMenuItems = $('a', sideMenu);
  sideMenuMinY = $('.page').position().top;
  lastSideMenuY = -1;
  currentSideMenuItem = 0;
  /* Enable prettyprint
  */

  if (typeof window.prettyPrint === "function") {
    window.prettyPrint();
  }
  /* Collapsable contentelements
  */

  $('.collapsor').click(function(e) {
    var self;
    e.preventDefault();
    self = $(this);
    return self.slideUp(300, function() {
      return $(self.attr('href')).slideDown(300);
    });
  });
  /* Smooth scroller for menu
  */

  $('.scrollToId').click(function(e) {
    e.preventDefault();
    return $('html, body').animate({
      scrollTop: $($(this).attr('href')).offset().top
    }, 800, 'swing');
  });
  /* Smallipop creation calls
  */

  $('.smallipop').smallipop();
  $('.smallipopOrange').smallipop({
    theme: 'orange'
  });
  $('.smallipopBlack').smallipop({
    theme: 'black'
  });
  $('.smallipopHideBlack').smallipop({
    theme: 'black',
    hideTrigger: true
  });
  $('.smallipopBlue').smallipop({
    theme: 'blue',
    invertAnimation: true
  });
  $('.smallipopBlueFatShadow').smallipop({
    theme: 'blue fat-shadow',
    invertAnimation: true
  });
  $('.smallipopHideBlue').smallipop({
    theme: 'blue',
    hideTrigger: true,
    popupYOffset: 40,
    invertAnimation: true
  });
  $('.smallipopWhite').smallipop({
    theme: 'white'
  });
  $('.smallipopHide').smallipop({
    hideTrigger: true,
    theme: 'white',
    popupYOffset: 5
  });
  $('.smallipopHideTrans').smallipop({
    hideTrigger: true,
    theme: 'white white-transparent',
    popupYOffset: 20
  });
  $('.smallipopStatic').smallipop({
    theme: 'black',
    popupDistance: 0,
    popupYOffset: -14,
    popupAnimationSpeed: 100
  });
  $('.smallipopBottom').smallipop({
    theme: 'black',
    preferredPosition: 'bottom'
  });
  $('.smallipopHorizontal').smallipop({
    preferredPosition: 'right',
    theme: 'black',
    popupOffset: 10,
    invertAnimation: true
  });
  $('.smallipopFormElement').smallipop({
    preferredPosition: 'right',
    theme: 'black',
    popupOffset: 0,
    triggerOnClick: true,
    popupId: 'form'
  });
  $('#tipcustomhint').smallipop({}, "I'm the real hint!");
  $(document).delegate('#tipkiller', 'click', function(e) {
    e.preventDefault();
    return $('#ajaxContainer').html('<div>Some new content</div>');
  });
  $(document).delegate('#tipkiller2', 'click', function(e) {
    e.preventDefault();
    return $('#ajaxContainer2').html('<div>Some new content</div>');
  });
  $('#tipChangeContent').smallipop({
    onAfterShow: function(trigger) {
      return $.smallipop.setContent(trigger, "I'm the new content and I have replaced the old boring content!");
    },
    onBeforeHide: function(trigger) {
      return $.smallipop.setContent(trigger, "Bye bye");
    }
  });
  $('#tipCSSAnimated').smallipop({
    cssAnimations: {
      enabled: true,
      show: 'animated bounceInDown',
      hide: 'animated hinge'
    }
  });
  $('#tipCSSAnimated2').smallipop({
    cssAnimations: {
      enabled: true,
      show: 'animated flipInX',
      hide: 'animated flipOutX'
    }
  });
  $('#tipCSSAnimated3').smallipop({
    cssAnimations: {
      enabled: true,
      show: 'animated fadeInLeft',
      hide: 'animated fadeOutRight'
    }
  });
  $('#tipCSSAnimated4').smallipop({
    cssAnimations: {
      enabled: true,
      show: 'animated rotateInDownLeft',
      hide: 'animated rotateOutUpRight'
    }
  });
  $('#tipDontHideOnTriggerClick').smallipop({
    hideOnTriggerClick: false
  });
  $('#tipDontHideOnContentClick').smallipop({
    hideOnPopupClick: false
  });
  /* Tour stuff
  */

  $('.smallipopTour').smallipop({
    theme: 'black wide',
    cssAnimations: {
      enabled: true,
      show: 'animated fadeIn',
      hide: 'animated fadeOut'
    }
  });
  $('#runTour').click(function() {
    return $('.smallipopTour').smallipop('tour');
  });
  $('#startExample1').click(function(e) {
    e.preventDefault();
    return $('#example2 .smallipopTour').smallipop('tour');
  });
  /* Animate smallipops when scrolling
  */

  if ($('.wobbler').length) {
    $(document).scroll(function() {
      var $win, $wobblers, scrollTop, winHeight;
      $wobblers = $('.wobbler:not(.wobble)');
      $win = $(window);
      winHeight = $win.height();
      scrollTop = $win.scrollTop();
      return $wobblers.each(function() {
        var $self, offset;
        $self = $(this);
        offset = $self.offset();
        if (offset.top > scrollTop + 50 && offset.top < scrollTop - 50 + winHeight) {
          return $self.addClass('wobble');
        }
      });
    }).trigger('scroll');
  }
  if (!develop) {
    try {
      piwikTracker = Piwik.getTracker('https://tracking.sebastianhelzle.net/piwik.php', 5);
      piwikTracker.trackPageView();
      return piwikTracker.enableLinkTracking();
    } catch (err) {

    }
  }
});
