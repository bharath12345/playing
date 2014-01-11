{{{
    "title": "Application Topology Graphs - Usecase, Different Product Offerings, Prototype Using D3 and jsPlumb",
    "tags" : [ "javascript", "graphs", "d3", "jsplumb" ],
    "category" : "technology",
    "date" : "09-01-2013",
    "description" : ""
}}}

Graph depictions are common for problems like computer networks, social networks etc. Sometime ago, I came across the use-case of graphs for software application topologies. This post covers the few things I discovered on the topic of application topologies and their graphical representation.

### Usecase
One aspect that makes application topologies a challenge is that they are *logical* and not *physical*. That is, the boundaries of a distributed application are difficult to define. In web companies/banks it is usual to find one *application-owner* just responsible for the database system while many other for applications that make use of the database. The database thus becomes a shared application with multiple owners/users. From the point of view of graphical representation of applications in such an enterprise, the representation of running application thus becomes 'logical' - one user might want to see his application topology include the database while another may not. The database-system application owner might want to see graphs that show just the clustered databases with their external interfaces and/or graphs which include the in-enterprise applications. Thus depending upon the organization hierarchies different application components may be required to be grouped differently (both hardware resources components like servers and software components like application servers ). Inter and intra application views are required. And different users and user groups may require different *layer-transitions* starting with a view of their application of ownership - both drilling-in and drilling-out - through the maze of applications and its constituents. Many Application Performance Management (APM) products claim to provide such graphical views. I take a look at their offerings in a later section in this blog where I look at the application graph views of popular APM vendors like AppDynamics, OpTier etc

### Prototype
Before getting too deep into thinking about application graphs I decided to develop a prototype for such graph representations. Unsurprisingly, just after a few hours of looking through the world of JavaScript discovered multiple libraries capable of good graph rendering. [This](http://stackoverflow.com/questions/7034/graph-visualization-code-in-javascript) StackOverflow thread is useful. One can buy good commercial graph rendering libraries like [yFiles](http://www.yworks.com) or use open-source freewares like [GraphDracula](http://www.graphdracula.net), mxGraph etc. But the libraries that I was most impressed with were [D3](http://www.d3js.org) and [jsPlumb](http://www.jsplumbtoolkit.com). I have played with D3 for over a year now and it is *the* most exciting JavaScript library for me on this planet! The very paradigm of data-modeling and programming for D3 is enlightening and it provides for extremely vivid and smooth graphical representations of all kind. And coming to jsPlumb, just a visit to the website is good enough to excite any programmer of its potential. So I got cracking with D3 and jsPlumb. Below are the two graph prototypes I came up with (it did not take much of an effort to code these using help from existing code available on web). The code for these prototypes are available on my GitHub repository too. I have used [Dojo](http://dojotoolkit.org/) for modularising the code (AMD way) and draw up the containers.

<link rel="stylesheet" type="text/css" href="/lib/my/topograph/topograph.css"/>
<div id="graphs" style="width: 1150px; height: 450px; border: 1px solid black;"></div>
&nbsp;

#### About these graphs
<div class="bs-docs-grid" id="about">
    <div class="row show-grid">
        <div class="col-md-6 left alignCenter"><h5>D3 Graph</h5></div>
        <div class="col-md-6 right alignCenter"><h5>jsPlumb Graph</h5></div>
    </div>
    <div class="row show-grid">
        <div class="col-md-6 left">
            <ol>
                <li>This is more like a inter-applications and application-group view</li>
                <li>The two different types of icons stand for single-applications (orange text) and application-groups (blue text)</li>
                <li>The graph can be dragged and zoomed. To drag/pan click on the graph and drag it. To zoom use the mouse scroller</li>
                <li>The graph actually represents a set of interconnected applications and application-groups</li>
                <li>With D3 it is not very difficult to add hover effect on nodes and links atop such a graph. It is not difficult to add color effects to application nodes and edges to signify status</li>
                <li>The icons, text and links are all SVG - so they scale beautifully on zooming</li>
                <li>Every refresh of the page leads to a re-rendering of the graph in a different way. This is so because the graph is rendered using [D3 Force directed graph](http://bl.ocks.org/mbostock/4062045) layout. The position of nodes and edges is not fixed but computed each time the page is rendered by the algorithm for the specified gravity, distance and charge configurations (this prototype is not a thorough job of getting the nitty-gritty of a force layout with D3 right for the best possible rendering within the coordinates of a box. Thoughtful tuning of parameters should make the graph good for all form factors and far better than I show here)</li>
            </ol>
        </div>
        <div class="col-md-6 right">
            <ol>
                <li>This is more like a intra-application view</li>
                <li>This depicts a typical web-application with its 3-tiers: web-layer, app-layer and datasource (database, external etc)</li>
                <li>jsPlumb provides many different types of connectors and endpoints. After playing with the options for a while I have the left the connections to look like 'Z' simply because it looked nice to me! (the more appropriate links would probably be straight lines, but this is just a playful prototype!). Have chosen the source endpoint of the connections to have a blue dot. The connections have an arrow on top (there are many choices for such settings)</li>
                <li>Mouse-over the links to see the color change from yellow to blue - this is just using a simple css setting</li>
                <li>To differentiate the 3-layers, I have internally used Dojo Titlepane's. I have a liking for their neat rendering</li>
                <li>The icons are SVG. Did not try to implement zoom, pan or node/link movement. They are very much doable though non-trivial</li>
            </ol>
        </div>
    </div>
</div>

#### Comparison
<div class="bs-docs-grid" id="comparison">
    <div class="row show-grid">
        <div class="col-md-2 first"></div>
        <div class="col-md-5 left alignCenter"><h5>D3</h5></div>
        <div class="col-md-5 right alignCenter"><h5>jsPlumb</h5></div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Scalability</h5></div>
        <div class="col-md-5 left">D3 is built for scalability of visual components. Hundreds and thousands of nodes and edges can be quickly created/updated/removed and the visualizations render and transition really fast (I did a quick scale test of close to 5000 nodes and few hundred thousand edges - one has to really see to believe how fast the rendering is)</div>
        <div class="col-md-5 right">jsPlumb is much slower than D3 in rendering. However that does not mean jsPlumb is slow - D3 is simply too fast!</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Layouts: Force etc</h5></div>
        <div class="col-md-5 left">D3 has pre-built [force layout](https://github.com/mbostock/d3/wiki/Force-Layout) visualization with many options. A force directed graph works beautifully when the real-estate available for rendering is dynamic along with a (probable) huge number of nodes and edges. The graph layout optimizes itself (per gravity/distance/charge settings) to provide the best possible view</div>
        <div class="col-md-5 right">jsPlumb provides endpoints and connectors. One can use the facilities to build a force directed graph but such rendering algorithms are not provided OOTB (coding a force layout algorithm is not trivial). However if the number of edges and nodes is known, is not very huge and falls into a clean pattern (like the 3-layers in the above graph), jsPlumb can be used to create very neat layouts</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Visual Beauty</h5></div>
        <div class="col-md-5 left">Requires programming. One can search and lookup upteen amazing D3 visualizations including many that are graphs. One can use SVG for scalable zooming. However, building a beautiful graph framework for a product with D3 will require some work</div>
        <div class="col-md-5 right">Even the default setting can produce excellent looking graphs. Building better looking graphs (with fewer elements) should be considerably easier with jsPlumb</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Development Simplicity</h5></div>
        <div class="col-md-5 left">D3 takes some learning. The paradigm of create/update/destroy of elements along with modelling of json data for a particular library function can be complex. But once the mind gets used to the paradigm one realizes its power and simplicity. Compared to all the JS visualization frameworks that I have used (Dojo, jQuery, Raphael, mootools, YUI, Google toolkit, FusionCharts etc) D3 is in a class of its own. Once you get hooked to creating charts/visuals the D3 way, I bet you wont go near anything else!</div>
        <div class="col-md-5 right">jsPlumb is truly simple. As a well thought out, well written and well documented library, one can start building working graphs in less than a day (which would be quite a challenge for D3 newbie to accomplish)</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Rendering Speed</h5></div>
        <div class="col-md-5 left">No other JS framework in that I have come across comes even in the vicinity of D3 in speed and performance. D3 is a class act.</div>
        <div class="col-md-5 right">Definitely not slow</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Layer transitions, Panning, Zoom</h5></div>
        <div class="col-md-5 left">D3 is built for zoom, pan like functionality from bottoms-up. The transitions are smooth, fast and just work</div>
        <div class="col-md-5 right">Requires some doing</div>
    </div>
    <div class="row show-grid">
        <div class="col-md-2 first"><h5>Project Liveness, Community, Future roadmap</h5></div>
        <div class="col-md-5 left">Super active. Its one of the most cloned projects in the JS world on GitHub. There is a large community of users and questions are quickly answered on StackOverflow, Google groups etc. With such strong foundations, I dont see the momentum behind D3 slowing down in near future</div>
        <div class="col-md-5 right">Not as hot as D3 but nevertheless very popular. Enjoys a fairly large community of users and in the tradition of jQuery plugin's one can easily see, understand, tweak the library's code which seems straightforward to understand for good developers on a demanding projects</div>
    </div>
</div>

### APM Products

Along with trying to understand application topologies and design this prototype I had a look at the offerings of some of the APM vendors. All top vendors advertise topology graphs but their offerings seem very limited - lot of constraints to both configuration and usage. One can see the screenshots from these products below. 

After looking at the existing offerings and my study, here is a dump of features that would be required for anyone attempting the challenge of application topology views -
<ol>
    <li>The nodes in the topology are representative of the hardware/software components. The links in the topology are representative of transactions. Corresponding status by colouring is much required</li>
    <li>Multi-level Application Groups are needed</li>
    <li>Heterogeneous groups of applications and application-groups with customizable drill-throughs makes a lot of sense</li>
    <li>Generally, in actual deployments n:n mapping between Application and Application-group is 'soft' or 'tag-like'. Application ownership and deployment structure often keeps changing. Users thus want to easily create new application-groups and add/remove applications from existing groups (all the time). This calls for a very flexible model the kind of which is not to be seen in existing product offerings</li>
    <li>Different users would want to see application topology's with different applications and groups in them. Since the whole idea of Application Topology is logical and per a particular user's world-view (and not something physical) - a user would have multiple topology views with some applications and groups present in many. Example, a user could define -
        <ol>
            <li>Topology Layer 'A' with 2 applications - 'CRM', 'Core' - and 2 application groups - 'InternalBusinessApps', 'InternalOperationsApps'</li>
            <li>Topology Layer 'B' with 1 application - 'Core' and 4 application groups - 'InternalBusinessApps', 'InternalOperationsApps', 'CustomerFacingApp', 'CriticalInterfacingApps'</li>
            <li>So now, between Layer 'A' and Layer 'B' there is one overlapping application and 2 overlapping application-groups</li>
        </ol>
     </li>
    <li>'Transactions, both intra and inter application, are typically HTTP(s), TCP, web-services, RMI/RPC etc</li>
    <li>Users may require links in different layers to have a configurable set of transactions mapped on them. Going back to the earlier example of layers 'A' and 'B' - the link between CRM and InternalBusinessApps in layer-A can be configured to show the status per a configured set of Transactions, say TxA and TxB. While the link between the same CRM and InternalBusinessApps in layer-B can be configured to show the status per TxB and TxC</li>
    <li>Users may require nodes in different layers to have a configurable set of hardware/software components mapped on them. Going back to the earlier example of layers 'A' and 'B' - the node for CRM in layer-A can be configured to show the status per a configured set of Components, say ServerA and DatabaseB. While the same CRM in layer-B can be configured to show the status per DatabaseB and AppServerC</li>
    <li>Once a user defines multiple layers of topology he needs to stitch the transition. This transition stitching is a very complex requirement. Apart from it being a configurable option, this action requires a default which will show a topology layer of all individual application constituents of a Application Group</li>
</ol>

<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.9.1/dojox/image/resources/image.css" media="screen">
<div id="apm"></div>

&nbsp;

### Concluding Thoughts
- Application topology is a wonderful emerging playground for those interested in graph representations. Its very fluid with many usecases and user expectations. Applications in enterprises are getting more and more distributed with more moving parts and complexity (while probably, computer networks in enterprises is progressively getting simplified thanks to bigger routers and switches!) thus making the problem of graphing them very exciting and challenging
- Open-source, liberally licensed JavaScript graphing toolkits like D3 and jsPlumb have really come of age to be used deep and wide in software products. Sufficiently interested and skilled programmers can do as good a job with these libraries as what is possible by using commercial packages like yFiles  

&nbsp;