import React from "react";
import {Grid, View} from '@adobe/react-spectrum';

const App = () => {
  return React.createElement(Grid, {areas:['header','content','footer'], rows: ['size-500', 'auto', 'size-500'], height: "800px", gap:"size-100"}, [
           React.createElement(View, {backgroundColor:"purple-600", gridArea:"header"}, "Bharadwaj"),
           React.createElement(View, {}),
           React.createElement(View, {backgroundColor:"gray-500", gridArea:"footer"}, "Powered by GitHub, Heroku, Scala, Play and React!")
         ])
};

export default App;
