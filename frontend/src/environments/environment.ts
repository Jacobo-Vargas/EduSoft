export const environment = {
  production: false,

  // Endpoints
  apiUrl: 'https://localhost:8443/api',
  apiUrl2: '/api-public',
  urlServer: 'https://localhost:8443/api',
  urlLogout: 'https://localhost:8443/api/auth/logout',
  urlLogin: '/auth/login',
  urlSendCodeEmail: '/auth/sendCodeEmail',
  urlRecoverPassword: '/auth/recover-password',
  urlUpdatePassword: '/auth/updatePassword',

  // Temporary roles subject to change
  roles: {
    admin: 'ROLE_ADMIN'
  }
};
