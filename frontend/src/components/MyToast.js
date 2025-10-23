import React from 'react';
import { Toast } from 'react-bootstrap';

const toastCss = {
  position: 'fixed',
  top: '70px',
  right: '20px',
  zIndex: 9999
};

const MyToast = ({ show, message, type = 'success' }) => {
  const variant = type || 'success';
  return (
    <div style={show ? toastCss : { display: 'none' }}>
      <Toast className={`border border-${variant} bg-${variant} text-white`} show={show} delay={3000} autohide>
        <Toast.Body>{message}</Toast.Body>
      </Toast>
    </div>
  );
};

export default MyToast;
