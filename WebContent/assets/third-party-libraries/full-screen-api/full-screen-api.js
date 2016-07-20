(function() {
    var
        fullScreenApi = {
            supportsFullScreen: false,
            nonNativeSupportsFullScreen: false,
            isFullScreen: function() { return false; },
            requestFullScreen: function() {},
            cancelFullScreen: function() {},
            toggleFullScreen: function() {},
            fullScreenEventName: '',
            prefix: ''
        },
        browserPrefixes = 'webkit moz o ms khtml'.split(' ');
// check for native support
    if (typeof document.cancelFullScreen != 'undefined') {
        fullScreenApi.supportsFullScreen = true;
    } else {
  // check for fullscreen support by vendor prefix
        for (var i = 0, il = browserPrefixes.length; i < il; i++ ) {
            fullScreenApi.prefix = browserPrefixes[i];
 
            if (typeof document[fullScreenApi.prefix + 'CancelFullScreen' ] != 'undefined' ) {
                fullScreenApi.supportsFullScreen = true;
 
                break;
            }
        }
    }
// update methods to do something useful
    if (fullScreenApi.supportsFullScreen) {
        fullScreenApi.fullScreenEventName = fullScreenApi.prefix + 'fullscreenchange';
 
        fullScreenApi.isFullScreen = function() {
            switch (this.prefix) {
                case '':
                    return document.fullScreen;
                case 'webkit':
                    return document.webkitIsFullScreen;
                case 'moz':
                    return document.mozFullScreen;
                default:
                    return document[this.prefix + 'FullScreen'];
            }
        };
        fullScreenApi.requestFullScreen = function(el) {
            return (this.prefix === '') ? el.requestFullScreen() : el[this.prefix + 'RequestFullScreen']();
        };
        fullScreenApi.cancelFullScreen = function(el) {
            return (this.prefix === '') ? document.cancelFullScreen() : document[this.prefix + 'CancelFullScreen']();
        };
    } else if (typeof window.ActiveXObject !== "undefined") {
        fullScreenApi.nonNativeSupportsFullScreen = true;
        
        fullScreenApi.requestFullScreen = function (el) {
        	var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        };
        fullScreenApi.isFullScreen = function() {
            return document.body.clientHeight == screen.height && document.body.clientWidth == screen.width;
        };
   }
    
   fullScreenApi.toggleFullScreen = function(el) {
       if (this.isFullScreen()) {
           return this.cancelFullScreen(el);
       } else {
           return this.requestFullScreen(el);
       }
   };
 
   // jQuery plugin
   if (typeof jQuery != 'undefined') {
       jQuery.fn.requestFullScreen = function() {
 
           return this.each(function() {
               if (fullScreenApi.supportsFullScreen) {
                   fullScreenApi.requestFullScreen(this);
               }
           });
       };
   }
 
   // export api
   window.fullScreenApi = fullScreenApi;
})();
//- See more at: http://xme.im/display-fullscreen-website-using-javascript#sthash.o4jOOPRl.dpuf