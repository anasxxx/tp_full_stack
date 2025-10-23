import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import NavigationBar from './components/NavigationBar';
import Bienvenue from './components/Bienvenue';
import Voiture from './components/Voiture';
import VoitureListe from './components/VoitureListe';
import Footer from './components/Footer';

const marginTop = {
  marginTop: '20px'
};

function App() {
  return (
    <Router>
      <NavigationBar />
      <Container>
        <Row>
          <Col lg={12} style={marginTop}>
            <Switch>
              <Route path="/" exact component={Bienvenue} />
              <Route path="/add" exact component={Voiture} />
              <Route path="/list" exact component={VoitureListe} />
            </Switch>
          </Col>
        </Row>
      </Container>
      <Footer />
    </Router>
  );
}

export default App;
