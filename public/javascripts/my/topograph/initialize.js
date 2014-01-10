/**
 * Created with JetBrains WebStorm.
 * User: bharadwaj
 * Date: 29/08/13
 * Time: 12:23 PM
 *
 * A Model for users
 * @module  initialize
 * @exports initialize
 * @extends none
 */
define(["dojo/_base/declare", "dijit/layout/ContentPane", "dijit/layout/BorderContainer", "dijit/TitlePane",
    "dojox/image/Gallery", "dojo/data/ItemFileReadStore", "dojo/parser", "dojo/dom-construct"],
    /** @lends module:initialize */
    function (declare, ContentPane, BorderContainer, TitlePane, Gallery, ItemFileReadStore, parser, domConstruct) {

        var initialize = declare("initialize", null, {

            /**
             * Returns a Dojo Titlepane
             * @memberOf module:initialize#
             * @param {string} region placement of the titlepane
             * @param {boolean} splitter boolean for splitter
             * @param {string} style style string if any
             * @param {string} name name in the title bar
             * @param {string} id dom id of the element
             * @return {*}
             */
            getContentPane: function(region, splitter, style, name, id) {
                return new TitlePane({
                    id:id+"Pane",
                    region:region,
                    splitter:splitter,
                    style:style,
                    content:"<div id='" + id + "' style='width: 100%; height: 100%;'></div>",
                    title:name,
                    toggleable:false
                });
            },

            /**
             * Returns a Dojo BorderContainer
             * @memberOf module:initialize#
             * @return {*}
             */
            getBorderContainer: function() {
                return new BorderContainer({
                    design:"sidebar",
                    liveSplitters:false,
                    persist:true,
                    gutters:false,
                    style: "width: 100%; height: 100%;"
                });
            },

            /**
             * Accept a list of Dojo ContentPane's, add them to the passed BorderContainer and start them all up
             * @memberOf module:initialize#
             * @param {BorderContainer} Dojo BorderContainer Object
             * @param {Array} list of Dojo ContentPane's
             * @param {DomNode} dom node to place BorderContainer at
             */
            addCPstartBC: function(bc, cpList, paneToPlace) {
                for(var i=0; i< cpList.length; i++) {
                    bc.addChild(cpList[i]);
                }
                bc.placeAt(paneToPlace);
                bc.startup();
                bc.resize();
            },

            /**
             * Create the skeleton for the two graph objects to be shown
             * @memberOf module:initialize#
             * @return {*}
             */
            initialize: function() {
                var bc = this.getBorderContainer();
                var cpList = [];
                cpList.push(this.getContentPane("left",false,"height:100%;width:50%", "D3 Graph", "D3AppGraph"));
                cpList.push(this.getContentPane("center",false, "", "jsPlumb Graph", "JsPlumbAppGraph"));
                this.addCPstartBC(bc, cpList, dojo.byId("graphs"));
                return cpList[1];
            },

            /**
             * Display other APM tools graph images using Dojo image gallery
             * @memberOf module:initialize#
             */
            createImageGallery: function() {
                var imageItemStore = new ItemFileReadStore({url: "../../lib/my/topograph/json/images.json"});
                var node = new Gallery({id: "gallery1",
                    pageSize: 5,
                    imageHeight: 500, imageWidth: 700,
                    style: "height: 500px; width: 700px; margin-left: 200px;"}, "apm");

                var itemRequest = {
                    query: {},
                    count: 20
                };
                var itemNameMap = {
                    imageThumbAttr: "thumb",
                    imageLargeAttr: "large"
                };
                node.setDataStore(imageItemStore, itemRequest, itemNameMap);
            }

        });

        return initialize;
    }
);
