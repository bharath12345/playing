(this.webpackJsonpcustomerapp=this.webpackJsonpcustomerapp||[]).push([[0],{25:function(e,t,n){},26:function(e,t,n){},33:function(e,t,n){"use strict";n.r(t);var s=n(0),r=n.n(s),a=n(18),i=n.n(a),o=(n(25),n.p,n(26),n(9)),c=n(10),u=n(12),l=n(11),d=n(1),m=function(e){Object(u.a)(n,e);var t=Object(l.a)(n);function n(){return Object(o.a)(this,n),t.apply(this,arguments)}return Object(c.a)(n,[{key:"deleteRow",value:function(e){console.log("row ",this.props,e),this.props.delEvent(e)}},{key:"render",value:function(){var e=this,t=this.props.customer;return Object(d.jsxs)("div",{className:"row",children:[t.firstName," \xa0",t.lastName,Object(d.jsx)("button",{type:"button",onClick:function(){return e.deleteRow(t.id)},children:"Delete"})]})}}]),n}(s.Component);function f(e){return Object(d.jsx)("div",{children:Object(d.jsx)("input",{type:"text",placeholder:"search by name",onChange:function(t){return e.filterEvent(t.target.value)}})})}var j=function(e){Object(u.a)(n,e);var t=Object(l.a)(n);function n(e){var s;return Object(o.a)(this,n),(s=t.call(this,e)).state={customers:[{id:1,firstName:"Rachel",lastName:"Green ",gender:"female",address:"Blore"},{id:2,firstName:"Chandler",lastName:"Bing",gender:"male",address:"West Street"},{id:3,firstName:"Joey",lastName:"Tribbiani",gender:"male",address:"Kattegat"},{id:4,firstName:"Monica",lastName:"Geller",gender:"female",address:"some address"},{id:5,firstName:"Ross",lastName:"Geller",gender:"male",address:"some address "},{id:6,firstName:"Phoebe",lastName:"Buffay",gender:"female",address:"some address"}]},s.state.original=s.state.customers,s}return Object(c.a)(n,[{key:"filterCustomers",value:function(e){var t=this.state.original.filter((function(t){return t.lastName.toUpperCase().indexOf(e.toUpperCase())>=0}));this.setState({customers:t})}},{key:"deleteCustomer",value:function(e){var t=this.state.customers.filter((function(t){return t.id!=e}));this.setState({customers:t},(function(){return console.log("customer deleted ",e)}))}},{key:"render",value:function(){var e=this;return Object(d.jsxs)("div",{children:[Object(d.jsx)(f,{filterEvent:function(t){return e.filterCustomers(t)}}),this.state.customers.map((function(t){return Object(d.jsx)(m,{delEvent:function(t){return e.deleteCustomer(t)},customer:t},t.id)}))]})}}]),n}(s.Component),p=n(19),h=n(2);var b=function(){return Object(d.jsx)("div",{className:"App",children:Object(d.jsx)(p.a,{children:Object(d.jsxs)(h.c,{children:[Object(d.jsx)(h.a,{path:"/list",component:j}),Object(d.jsx)(h.a,{path:"/todo",component:j}),Object(d.jsx)(h.a,{exact:!0,path:"/",component:j}),Object(d.jsx)(h.a,{default:!0,component:j})]})})})},v=function(e){e&&e instanceof Function&&n.e(3).then(n.bind(null,34)).then((function(t){var n=t.getCLS,s=t.getFID,r=t.getFCP,a=t.getLCP,i=t.getTTFB;n(e),s(e),r(e),a(e),i(e)}))};i.a.render(Object(d.jsx)(r.a.StrictMode,{children:Object(d.jsx)(b,{})}),document.getElementById("root")),v()}},[[33,1,2]]]);
//# sourceMappingURL=main.36fc33f1.chunk.js.map