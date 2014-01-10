/**
 * Created with JetBrains WebStorm.
 * User: bharadwaj
 * Date: 28/08/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */

define(["dojo/_base/declare", "dojox/layout/GridContainer", "dijit/TitlePane", "dijit/layout/ContentPane", "dojo/request/xhr", "dojo/_base/lang"],
    /** @lends jsPlumbAppGraph */
    function (declare, GridContainer, TitlePane, ContentPane, xhr, lang) {

        /**
         * Creates the connectivity on jsPlumb graph
         * @class renderConnectivity
         * @exports my/jsPlumbAppGraph/renderConnectivity
         */
        var renderConnectivity = declare("renderConnectivity", null, {

            /**
             * @constructor
             * @function
             */
            constructor:function () {
                jsPlumb.importDefaults({
                    // default drag options
                    DragOptions:{ cursor:'pointer', zIndex:2000 },

                    // default to blue at one end and green at the other
                    EndpointStyles:[
                        { fillStyle:'#225588' },
                        { fillStyle:'#558822' }
                    ],

                    // blue endpoints 7 px; green endpoints 11.
                    Endpoints:[
                        [ "Dot", {radius:3} ],
                        [ "Dot", { radius:3 } ]
                    ],

                    // the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
                    // case it returns the 'labelText' member that we set on each connection in the 'init' method below.
                    ConnectionOverlays:[
                        [ "Arrow", { location:0.9, foldback:0.5, width:6 } ],
                        [ "Label", {
                            location:0.1,
                            id:"label",
                            cssClass:"aLabel"
                        }]
                    ]
                });
            },

            /**
             * Creates the connectivity on jsPlumb graph
             * @function
             * @param input
             */
            create:function (input) {

                for (var i = 0; i < input.length; i++) {
                    var lhsDivName = input[i].nodes[0]  + renderNodes.ENDPOINT_SUFFIX;
                    var rhsDivName = input[i].nodes[1] + renderNodes.ENDPOINT_SUFFIX;
                    console.log("lhs div name = " + lhsDivName + " rhs div name = " + rhsDivName);

                    // create Endpoint on LHS and RHS node
                    var srcUuid = renderConnectivity.FROM + input[i].nodes[0] + renderConnectivity.TO + input[i].nodes[1];
                    var dstUuid = renderConnectivity.FROM + input[i].nodes[1] + renderConnectivity.TO + input[i].nodes[0];
                    console.log("src uuid = " + srcUuid + " dst uuid = " + dstUuid);

                    var sourceEP = jsPlumb.addEndpoint(lhsDivName,
                        renderConnectivity.sourceEndpoint,
                        {
                            uuid:srcUuid,
                            container:jsPlumbAppGraph.PANE.domNode,
                            anchor:"RightMiddle"
                        });


                    var dstEP = jsPlumb.addEndpoint(rhsDivName,
                        renderConnectivity.targetEndpoint,
                        {
                            uuid:dstUuid,
                            container:jsPlumbAppGraph.PANE.domNode,
                            anchor:"LeftMiddle"
                        });

                    // create connection link
                    jsPlumb.connect({ source:sourceEP, target:dstEP, container:jsPlumbAppGraph.PANE.domNode });

                    // get and set the status of the connection link
                    var viewMeta = {
                        id:"", // uuid of the link?
                        name:"" // uuid of the link?

                    };
                }
            }
        });

        renderConnectivity.FROM = "FROM_";
        renderConnectivity.TO = "_TO_";

        // this is the paint style for the connecting lines..
        renderConnectivity.connectorPaintStyle = {
            lineWidth:1,
            strokeStyle:"#deea18",
            joinstyle:"round",
            outlineColor:"#EAEDEF",
            outlineWidth:1
        };

        // .. and this is the hover style.
        renderConnectivity.connectorHoverStyle = {
            lineWidth:2,
            strokeStyle:"#2e2aF8"
        };

        // the definition of source endpoints (the small blue ones)
        renderConnectivity.sourceEndpoint = {
            endpoint:"Dot",
            paintStyle:{ fillStyle:"#225588", radius:3 },
            isSource:true,
            connector:[ "Straight", { stub:[40, 60], gap:10 } ],
            connectorStyle:renderConnectivity.connectorPaintStyle,
            hoverPaintStyle:renderConnectivity.connectorHoverStyle,
            connectorHoverStyle:renderConnectivity.connectorHoverStyle,
            dragOptions:{},
            overlays:[
                [ "Label", {
                    location:[0.5, 1.5],
                    label:"",
                    cssClass:"endpointSourceLabel"
                } ]
            ]
        };

        renderConnectivity.targetEndpoint = {
            endpoint:"Dot",
            paintStyle:{ fillStyle:"#558822", radius:3 },
            hoverPaintStyle:renderConnectivity.connectorHoverStyle,
            maxConnections:-1,
            dropOptions:{ hoverClass:"hover", activeClass:"active" },
            isTarget:true,
            overlays:[
                [ "Label", { location:[0.5, -0.5], label:"", cssClass:"endpointTargetLabel" } ]
            ]
        };

        /**
         * Node class
         * @class renderNodes
         * @exports my/jsPlumbAppGraph/renderNodes
         */
        var renderNodes = declare("renderNodes", null, {

            /**
             * Returns SVG Icons
             * @function
             * @param src
             * @param width
             * @param height
             * @return {*}
             */
            getSvgIcon:function (src, width, height) {
                var svgIcon = dojo.create("img");
                svgIcon.src = src;
                //svgIcon.type = "image/svg+xml";
                svgIcon.height = width;
                svgIcon.width = height;
                return svgIcon;
            },

            /**
             * Creates the graph endpoint with appropriate SVG image
             * @function
             * @param endPointsArray
             * @param endPointType
             * @param width
             * @param height
             */
            createEndpoint:function (endPointsArray, endPointType, width, height) {
                var styleString = "width: " + width + "; height: " + height + ";";
                for (var i = 0; i < endPointsArray.length; i++) {
                    var divCol = dojo.byId(endPointType + renderNodes.ROW_SUFFIX);
                    var node = dojo.create("div");
                    node.id = endPointType + "_" + endPointsArray[i] + renderNodes.ENDPOINT_SUFFIX;
                    node.className = "topoIconContainer";
                    node.style.cssText = styleString;
                    divCol.appendChild(node);
                    //var endpoint = jsPlumb.addEndpoint(endPointName);
                    //TOPOLOGY.NODEMAP[endPointName] = endpoint;

                    var imgW = 50, imgH = 50;
                    switch (endPointType) {
                        case renderNodes.TYPE.WEBSERVER:
                            var svgIcon = this.getSvgIcon("../../images/topograph/osa_server_web.svg", imgW, imgH);
                            node.appendChild(svgIcon);
                            break;

                        case renderNodes.TYPE.APPSERVER:
                            var svgIcon = this.getSvgIcon("../../images/topograph/osa_server_application.svg", imgW, imgH);
                            node.appendChild(svgIcon);
                            break;

                        case renderNodes.TYPE.DATABASES:
                            var svgIcon = this.getSvgIcon("../../images/topograph/osa_database.svg", imgW, imgH);
                            node.appendChild(svgIcon);
                            break;

                        case renderNodes.TYPE.MESSAGEQ:
                            var svgIcon = this.getSvgIcon("../../images/topograph/Messaging_Queue.svg", imgW, imgH);
                            node.appendChild(svgIcon);
                            break;

                        case renderNodes.TYPE.TCPENDPOINTS:
                            var svgIcon = this.getSvgIcon("../../images/topograph/osa_ics_drive.svg", imgW, imgH);
                            node.appendChild(svgIcon);
                            break;

                        default:
                            console.log("unknown end point type = " + endPointType);
                            return;
                    }
                }
            },

            /**
             * Creates inner column panes for jsPlumb layout
             * @function
             * @param names
             * @param width
             * @param height
             */
            createColumnPanes:function (names, width, height) {
                console.log("in createColumnPanes = " + (names.length) + " w = " + width + " h = " + height);
                try {
                    var styleString = "width: " + (width / (names.length)) + "; height: " + height + ";";

                    var titlePanes = [];
                    for (var i = 0; i < names.length; i++) {
                        console.log("new pane for = " + names[i]);
                        var titlePane = new ContentPane({
                            splitter:false,
                            style:styleString,
                            content:"<div id='" + names[i] + renderNodes.COLUMN_SUFFIX + "' style='width: 100%; height: 100%;'></div>",
                            title:names[i],
                            toggleable:false
                        });
                        titlePanes[i] = titlePane;
                    }

                    var gridContainer = new GridContainer({nbZones:names.length, isAutoOrganized:true,
                        style:"width: " + width + " height:" + height}, "JsPlumbAppGraph");
                    gridContainer.disableDnd();

                    var j = 0, k = 0;
                    for (var i = 0; i < names.length; i++) {
                        j = (i % (names.length));
                        k = parseInt(i / (names.length));
                        gridContainer.addChild(titlePanes[i], j, k);
                    }
                    gridContainer.startup();
                    gridContainer.resize();

                    this.removeMargins(gridContainer);

                } catch (e) {
                    console.log("exception = " + e);
                }
                //return titlePanes;
            },

            /**
             * Changes to default Dojo Titlepane layout to suit the graph
             * @function
             * @param gridContainer
             */
            removeMargins:function (gridContainer) {
                var innerPane = dojo.query(".dijitTitlePaneContentInner", gridContainer.domNode);
                //console.log("inner len = " + innerPane.length);
                for (var i = 0; i < innerPane.length; i++) {
                    innerPane[i].style.padding = 0;
                }

                var head = dojo.query(".dijitTitlePaneTitle", gridContainer.domNode)
                for (var i = 0; i < head.length; i++) {
                    head[i].style.padding = 0;
                    head[i].style.minHeight = 0;
                }

                var headFocus = dojo.query(".dijitTitlePaneTitleFocus", gridContainer.domNode)
                for (var i = 0; i < headFocus.length; i++) {
                    headFocus[i].style.margin = 0;
                    headFocus[i].style.padding = 0;
                }
            },

            /**
             * Creates inner panes
             * @function
             * @param layers
             * @param name
             * @param width
             * @param height
             * @return {Array}
             */
            createInnerPanes:function (layers, name, width, height) {
                try {
                    var hl = height / layers.length;
                    var styleString = "width: " + width + "; height: " + hl + ";";

                    var innerPanes = [];
                    for (var i = 0; i < layers.length; i++) {
                        var innerPane = new TitlePane({
                            splitter:false,
                            style:styleString,
                            content:"<div id='" + layers[i] + renderNodes.ROW_SUFFIX + "' style='" + styleString + "'></div>",
                            title:layers[i],
                            toggleable:false
                        });
                        innerPanes[i] = innerPane;
                    }

                    var gridContainer = new GridContainer({nbZones:1, isAutoOrganized:true}, dojo.byId(name + renderNodes.COLUMN_SUFFIX));
                    gridContainer.disableDnd();

                    for (var i = 0; i < layers.length; i++) {
                        gridContainer.addChild(innerPanes[i], 1, i);
                    }
                    gridContainer.startup();
                    gridContainer.resize();
                    this.removeMargins(gridContainer);

                } catch (e) {
                    console.log("exception inner page = " + e);
                }
                return innerPanes;
            },

            /**
             * Creates jsPlumb graph
             * @function
             * @param input
             */
            create:function (input) {

                var layerTypes = [];
                for (var i = 0; i < input.length; i++) {
                    layerTypes.push(input[i].layertype);
                }
                this.createColumnPanes(layerTypes, jsPlumbAppGraph.width, jsPlumbAppGraph.height);

                var colWidth = jsPlumbAppGraph.width / layerTypes.length;
                var divW = 60, divH = 60;
                for (var i = 0; i < input.length; i++) {
                    console.log("layer type = " + layerTypes[i]);

                    var layer = input[i].layer;
                    console.log("layer length = " + layer.length);
                    var layerNames = [];
                    for (var j = 0; j < layer.length; j++) {
                        console.log("layer inner name = " + layer[j].name);
                        layerNames.push(layer[j].name);
                    }
                    this.createInnerPanes(layerNames, layerTypes[i], colWidth, jsPlumbAppGraph.height);
                    for (var j = 0; j < layer.length; j++) {
                        this.createEndpoint(layer[j].value, layer[j].name, divW, divH);
                    }
                }

                var viewMeta = {
                    id:"",
                    name:""
                };

                xhr("../../lib/my/topograph/json/ConnectivityALL.json", {
                    handleAs:"json",
                    method:"GET",
                    query:viewMeta,
                    headers:jsPlumbAppGraph.JSON_HEADER
                }).then(lang.hitch(this, this.createConnectivity));

                var innerPane = dojo.query(".dijitTitlePaneContentOuter", jsPlumbAppGraph.PANE.domNode);
                for (var i = 0; i < innerPane.length; i++) {
                    innerPane[i].style.border = 0;
                }

                var gridText = dojo.query(".dijitTitlePaneTextNode", jsPlumbAppGraph.PANE.domNode);
                for (var i = 0; i < gridText.length; i++) {
                    gridText[i].style.display = "block";
                    gridText[i].style.textAlign = "center";
                }
            },

            /**
             * @function
             * @param input
             */
            createConnectivity:function (input) {
                new renderConnectivity().create(input);
            }
        });

        renderNodes.TYPE = {};
        renderNodes.TYPE.WEBSERVER = "WebServers";
        renderNodes.TYPE.APPSERVER = "AppServers";
        renderNodes.TYPE.DATABASES = "Databases";
        renderNodes.TYPE.MESSAGEQ = "MessageQueues";
        renderNodes.TYPE.TCPENDPOINTS = "TcpEndpoints";

        renderNodes.COLUMN_SUFFIX = "_col";
        renderNodes.ROW_SUFFIX = "_row";
        renderNodes.ENDPOINT_SUFFIX = "_endpoint";

        /**
         * Creates jsPlumb graph
         * @class
         * @exports my/topograph/jsPlumbAppGraph
         * @module jsPlumbAppGraph
         */
        var jsPlumbAppGraph = declare("jsPlumbAppGraph", null, {

            /**
             * launch point
             * @function
             * @param pane
             */
            launch:function (pane) {
                jsPlumbAppGraph.PANE = pane;
                var node = document.getElementById("JsPlumbAppGraphPane");
                jsPlumbAppGraph.width = node.clientWidth;
                jsPlumbAppGraph.height = node.clientHeight;
                console.log("view port width = " + jsPlumbAppGraph.width + " height = " + jsPlumbAppGraph.height);

                xhr("../../lib/my/topograph/json/Nodes.json", {
                    handleAs:"json",
                    method:"GET",
                    headers:jsPlumbAppGraph.JSON_HEADER
                }).then(lang.hitch(this, this.createNodeLayout));
            },

            /**
             * Callback from xhr
             * @function
             * @param input
             */
            createNodeLayout:function (input) {
                new renderNodes().create(input);
            }
        });

        jsPlumbAppGraph.JSON_HEADER = { 'Content-Type':'application/json' };

        jsPlumbAppGraph.width = 300;
        jsPlumbAppGraph.height = 300;

        jsPlumbAppGraph.PANE;

        return jsPlumbAppGraph;
    });