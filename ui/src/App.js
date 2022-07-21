import React, { Component } from "react";
import {Grid, View, defaultTheme, Provider} from '@adobe/react-spectrum';
import Client from "./Client";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = { title: "" };
  }

  async componentDidMount() {
    Client.getSummary((summary) => {
      this.setState({
        title: summary.content,
      });
    });
  }

  render() {
    return (
      <Provider theme={defaultTheme}>
        <Grid
          areas={['header','content','footer']}
          rows={['size-500', 'auto', 'size-500']}
          height="800px"
          gap="size-100">
          <View backgroundColor="purple-600" gridArea="header">Bharadwaj</View>
          <View gridArea="content" />
          <View backgroundColor="gray-500" gridArea="footer">Powered by GitHub, Heroku, Scala, Play and React!</View>
        </Grid>
      </Provider>
    );
  }
}

export default App;
