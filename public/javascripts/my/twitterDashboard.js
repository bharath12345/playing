/**
 * Created by bharadwaj on 26/02/14.
 */

require(["dijit/registry", "dojox/layout/GridContainer", "dijit/TitlePane", "dojo/domReady!"],
    function (registry, GridContainer, TitlePane) {
        console.log("going dojo")
        // create a new GridContainer:
        var gridContainer = new GridContainer({
            nbZones: 2,
            style: {width: '100%', height: '100%'},
            isAutoOrganized: true
        }, 'placeHere');

        for (var i = 0; i < 4; i++) {
            var chartId = "chart_" + i
            var innerContent = '<div class="chart" id=' + chartId + '></div>'

            var titlepane = new TitlePane({
                title: 'Twitter Trends Graph',
                content: innerContent,
                splitter: false
            });

            gridContainer.addChild(titlepane, i % 2, i / 2);
        }

        // startup GridContainer:
        gridContainer.startup();
        gridContainer.disableDnd(); // Disables DND
        gridContainer.resize()

        createChart()
        createWebsocket()
    });
