/*****************************************************************
 * @author: Juan Valencia Calvellido (calvellido),
 * juanvalencia@calvellido.es
 * https://github.com/calvellido
 *
 * headfoot for Reveal.js
 *
 * @license
 * MIT licensed
 ******************************************************************/

const RevealHeadfoot = window.RevealHeadfoot || (function () {

	const options = Reveal.getConfig().headfoot || {};
	options.path = options.path || scriptPath() || 'plugin/headfoot/';
	if (!options.path.endsWith('/')) {
		options.path += '/';
	}

	const init = function () {
		loadResource(options.path + 'headfoot.css', 'stylesheet');
	};

  // modified from math plugin
  function loadResource( url, type, callback ) {
    var head = document.querySelector( 'head' );
    var resource;

    if ( type === 'script' ) {
      resource = document.createElement( 'script' );
      resource.type = 'text/javascript';
      resource.src = url;
    }
    else if ( type === 'stylesheet' ) {
      resource = document.createElement( 'link' );
      resource.rel = 'stylesheet';
      resource.href = url;
    }

    // Wrapper for callback to make sure it only fires once
    var finish = function() {
      if( typeof callback === 'function' ) {
        callback.call();
        callback = null;
      }
    }

    resource.onload = finish;

    // Normal browsers
    head.appendChild( resource );
  }

	function scriptPath() {
		// obtain plugin path from the script element
		var path;
		if (document.currentScript) {
			path = document.currentScript.src.slice(0, -11);
		} else {
			var sel = document.querySelector('script[src$="headfoot.js"]')
			if (sel) {
				path = sel.src.slice(0, -11);
			}
		}
		return path;
	}

	return {
		init: init
	};

})();

Reveal.registerPlugin('headfoot', RevealHeadfoot);
