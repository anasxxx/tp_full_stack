import React, { Component } from 'react';
import { Container, Row, Col, Jumbotron } from 'react-bootstrap';

class Bienvenue extends Component {
  render() {
    return (
      <Jumbotron className="bg-dark text-white">
        <Container>
          <Row>
            <Col lg={12} className="text-center">
              <h1>Bienvenue dans votre Magasin de Voitures</h1>
              <p>Le meilleur de nos voitures exposées chez Master MIOLA.</p>
            </Col>
          </Row>
        </Container>
      </Jumbotron>
    );
  }
}

export default Bienvenue;
