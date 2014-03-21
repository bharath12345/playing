/**
 * Created with JetBrains WebStorm.
 * User: bharadwaj
 * Date: 28/08/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 *
 * D3 Graph implementation
 * @module  d3AppGraph
 * @exports d3AppGraph
 * @extends none
 */

define(["dojo/_base/declare"],
    /** @lends module:d3AppGraph */
    function (declare) {

        var d3AppGraph = declare("d3AppGraph", null, {

            /**
             * Creates the D3 Graph
             * @memberOf module:d3AppGraph#
             */
            launch: function() {

                this.nodes = this.getNodes();
                this.links = this.getLinks();

                var color = d3.scale.category10();

                var node = document.getElementById("D3AppGraphPane");
                var width = node.clientWidth - 50;
                var height = node.clientHeight - 50;
                console.log("view port width = " + width + " height = " + height);

                var vis = d3.select("#D3AppGraph").append("svg")
                    .attr("width", width)
                    .attr("height", height)
                    .attr("preserveAspectRatio", "xMidYMid meet")
                    .attr("pointer-events", "all")
                    .append('svg:g')
                    .call(d3.behavior.zoom().on("zoom", redraw))
                    .append('svg:g');

                vis.append('svg:rect')
                    .attr('width', width)
                    .attr('height', height)
                    .attr('fill', 'white');

                function redraw() {
                    vis.attr("transform",
                        "translate(" + d3.event.translate + ")"
                            + " scale(" + d3.event.scale + ")");
                };

                var force = self.force = d3.layout.force()
                    .nodes(this.nodes)
                    .links(this.links)
                    .gravity(.05)
                    .distance(200)
                    .charge(-500)
                    .size([width, height])
                    .start();

                var link = vis.selectAll("line.link")
                    .data(this.links)
                    .enter().append("svg:line")
                    .attr("class", "link")
                    .attr("x1", function(d) { return d.source.x; })
                    .attr("y1", function(d) { return d.source.y; })
                    .attr("x2", function(d) { return d.target.x; })
                    .attr("y2", function(d) { return d.target.y; });

                var node_drag = d3.behavior.drag()
                    .on("dragstart", dragstart)
                    .on("drag", dragmove)
                    .on("dragend", dragend);

                function dragstart(d, i) {
                    force.stop() // stops the force auto positioning before you start dragging
                }

                function dragmove(d, i) {
                    d.px += d3.event.dx;
                    d.py += d3.event.dy;
                    d.x += d3.event.dx;
                    d.y += d3.event.dy;
                    tick(); // this is the key to make it work together with updating both px,py,x,y on d !
                }

                function dragend(d, i) {
                    d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff
                    tick();
                    force.stop();
                }

                var node = vis.selectAll("g.node")
                    .data(this.nodes)
                    .enter().append("svg:g")
                    .attr("class", "node")
                    .call(node_drag);

                node.append("svg:image")
                    .attr("class", "node")
                    .attr("xlink:href", function (d) {
                        console.log('group for image in d = ' + d.group);
                        if(d.group == 1) {
                            return d3AppGraph.APPGROUPICON;
                        } else {
                            return d3AppGraph.APPICON;
                        }
                    })
                    .attr("x", "-8px")
                    .attr("y", "-8px")
                    .attr("width", "32px")
                    .attr("height", "32px");

                node.append("svg:text")
                    .attr("class", "nodetext")
                    .attr("dx", 12)
                    .attr("dy", ".35em")
                    .text(function(d) { return d.name })
                    .style("fill", function (d) {
                        return color(d.group);
                    });

                force.on("tick", tick);

                function tick() {
                    link.attr("x1", function(d) { return d.source.x; })
                        .attr("y1", function(d) { return d.source.y; })
                        .attr("x2", function(d) { return d.target.x; })
                        .attr("y2", function(d) { return d.target.y; });

                    node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
                };

                for (var i = 0; i < 100; ++i) force.tick();
                force.stop();
            },

            getNodes:function () {
                return d3AppGraph.nodes;
            },

            getLinks:function () {
                return d3AppGraph.links;
            }

        });

        d3AppGraph.nodes = [
            {"name":"CustomerFacingApp","group":1},
            {"name":"CriticalInterfaceApp","group":1},
            {"name":"CoreApplication","group":1},
            {"name":"InternalOperationsApp","group":1},
            {"name":"InternalBusinessApp","group":1},
            {"name":"CollectionApplication","group":2},
            {"name":"DisbursementApplication","group":2},
            {"name":"RegistrationApplication","group":2}];

        d3AppGraph.links =[
            {"source":0,"target":1,"value":1},

            {"source":1,"target":2,"value":2},
            {"source":1,"target":3,"value":2},
            {"source":1,"target":4,"value":2},

            {"source":3,"target":2,"value":3},
            {"source":4,"target":2,"value":3},

            {"source":4,"target":3,"value":4},
            {"source":3,"target":4,"value":4},

            {"source":5,"target":1,"value":5},
            {"source":6,"target":3,"value":5},
            {"source":7,"target":4,"value":5}];

        d3AppGraph.APPGROUPICON = "../../images/topograph/AppSet.128.png";
        d3AppGraph.APPICON = "../../images/topograph/AppGlobe.64.png";

        return d3AppGraph;
    });