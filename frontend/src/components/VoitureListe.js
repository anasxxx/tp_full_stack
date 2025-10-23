import React, { Component } from 'react';
import { Card, Table, ButtonGroup, Button, Spinner } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faList, faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import MyToast from './MyToast';
import api from '../api';

class VoitureListe extends Component {
  constructor(props) {
    super(props);
    this.state = {
      voitures: [],
      show: false,
      toastMessage: '',
      toastType: 'success',
      loading: false
    };
  }

  componentDidMount() {
    this.findAllVoitures();
  }

  findAllVoitures = () => {
    this.setState({ loading: true });
    api
      .get('/voitures')
      .then((response) => response.data)
      .then((data) => {
        this.setState({ voitures: data, loading: false });
      })
      .catch((error) => {
        console.error('Erreur lors du chargement des voitures', error);
        this.showToast(
          `Erreur lors du chargement des voitures : ${error.response?.status || ''} ${
            error.response?.statusText || ''
          }`,
          'danger'
        );
        this.setState({ loading: false });
      });
  };

  deleteVoiture = (voitureId) => {
    api
      .delete(`/voitures/${voitureId}`)
      .then(() => {
        this.setState(
          {
            voitures: this.state.voitures.filter((voiture) => voiture.id !== voitureId)
          },
          () => this.showToast('Voiture supprimée avec succès.', 'danger')
        );
      })
      .catch((error) => {
        console.error('Erreur lors de la suppression de la voiture', error);
        this.showToast('Erreur lors de la suppression de la voiture.', 'danger');
      });
  };

  showToast = (message, type = 'success') => {
    this.setState({ show: true, toastMessage: message, toastType: type });
    setTimeout(() => this.setState({ show: false }), 3000);
  };

  render() {
    const { voitures, show, toastMessage, toastType, loading } = this.state;

    return (
      <Card className="border border-dark bg-dark text-white">
        <MyToast show={show} message={toastMessage} type={toastType} />
        <Card.Header>
          <FontAwesomeIcon icon={faList} className="mr-2" /> Liste des Voitures
        </Card.Header>
        <Card.Body>
          {loading ? (
            <div className="text-center my-4">
              <Spinner animation="border" variant="light" />
            </div>
          ) : (
            <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Marque</th>
                <th>Modèle</th>
                <th>Couleur</th>
                <th>Matricule</th>
                <th>Immatricule</th>
                <th>Prix</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {voitures.length === 0 ? (
                <tr align="center">
                  <td colSpan="7">Aucune voiture n'est disponible.</td>
                </tr>
              ) : (
                voitures.map((voiture) => (
                  <tr key={voiture.id}>
                    <td>{voiture.marque}</td>
                    <td>{voiture.modele}</td>
                    <td>{voiture.couleur}</td>
                    <td>{voiture.matricule}</td>
                    <td>{voiture.immatricule}</td>
                    <td>{voiture.prix}</td>
                    <td>
                      <ButtonGroup>
                        <Button
                          size="sm"
                          variant="outline-primary"
                          as={Link}
                          to={{
                            pathname: '/add',
                            state: { voiture: voiture }
                          }}
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </Button>
                        <Button
                          size="sm"
                          variant="outline-danger"
                          className="ml-2"
                          onClick={() => this.deleteVoiture(voiture.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </Button>
                      </ButtonGroup>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>
    );
  }
}

export default VoitureListe;
