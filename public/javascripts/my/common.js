/**
 * Created with JetBrains WebStorm.
 * User: bharadwaj
 * Date: 28/08/13
 * Time: 10:02 AM
 * To change this template use File | Settings | File Templates.
 *
 * D3 Graph implementation
 * @module  common
 * @exports common
 * @extends none
 */

define(["dojo/_base/declare"],
    /** @lends module:common */
    function (declare) {

        var common = declare("common", null, {});

        /**
         * For proper Twitter Bootstrap grid layout.
         * @memberOf module:common#
         * @param {string} dom id of the node.
         */
        common.AdjustDivHeight = function(domNodeId) {
            var leftCol, rightCol, firstCol;
            if(domNodeId == null) {
                leftCol  = dojo.query('.left');
                rightCol = dojo.query('.right');
                firstCol = dojo.query('.first');
            } else {
                leftCol  = dojo.query("#" + domNodeId + ' .left');
                rightCol = dojo.query("#" + domNodeId + ' .right');
                firstCol = dojo.query("#" + domNodeId +' .first');
            }
            console.log("num of elements = " + leftCol.length);
            for(var i=0;i<leftCol.length;i++) {
                //console.log("setting right height to = " + leftCol[i].clientHeight);
                rightCol[i].style.height = leftCol[i].clientHeight+2+"px";
                if(firstCol[i]!=null) {
                    firstCol[i].style.height = leftCol[i].clientHeight+2+"px";
                }
            }
        };

        /**
         * Creates Table of Contents
         * @memberOf module:common#
         * @param {number} start level
         * @param {number} depth
         */
        common.GenerateTOC = function(startLevel, depth) {
            var entryBlock = jQuery("#entryBlock");
            var container = jQuery("<div id='tocBlock'></div>");
            entryBlock.append(container);

            var heading = 'Table of Contents';
            container.append('<span class="tocHeading">' + heading + '</span>');

            var div = jQuery("<ul id='toc' style='padding-left: 10px'></ul>");
            container.append(div);

            div.tableOfContents(document.body, {startLevel: startLevel, depth: depth});
        };

        return common;
    }
);