/**
  * Copyright 2011 The Closure Compiler Authors.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

/**
  * @fileoverview Externs for jQuery 1.7
  *
  * Note that some functions use different return types depending on the number
  * of parameters passed in. In these cases, you may need to annotate the type
  * of the result in your code, so the JSCompiler understands which type you're
  * expecting. For example:
  * <code>var elt = /** @type {Element} * / (foo.get(0));</code>
  *
  * @see http://api.jquery.com/
  * @externs
  */


/**
  * @constructor
  * @param {(jQuerySelector|Element|Object|Array.<Element>|jQuery|string|
  * function())=} arg1
  * @param {(Element|jQuery|Document|
  * Object.<string, (string|function(jQuery.event=))>)=} arg2
  * @return {jQuery}
  */
function jQuery(arg1, arg2) {}

/**
  * @constructor
  * @extends {jQuery}
  * @param {(jQuerySelector|Element|Array.<Element>|Object|jQuery|string|
  * function())} arg1
  * @param {(Element|jQuery|Document|
  * Object.<string, (string|function(jQuery.event=))>)=} arg2
  * @return {jQuery}
  */
function $(arg1, arg2) {}

/**
  * @param {(string|Object.<string,*>)} arg1
  * @param {Object.<string,*>=} settings
  * @return {jQuery.jqXHR}
  */
jQuery.ajax = function(arg1, settings) {};

/**
  * @param {(string|Object.<string,*>)} arg1
  * @param {Object.<string,*>=} settings
  * @return {jQuery.jqXHR}
  */
$.ajax = function(arg1, settings) {};

