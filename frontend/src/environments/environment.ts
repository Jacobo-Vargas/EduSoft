export const environment = {
  production: false,
  apiUrl: '/api',
  apiUrl2: '/api-public',
  urlLogin: '/auth/login',
  urlSendCodeEmail: '/auth/sendCodeEmail',
  urlRecoverPassword: '/auth/recover-password',
  urlUpdatePassword: '/auth/updatePassword',
  urlServer: 'http://localhost:8443/api',
  urlLogout: 'https://localhost:8443/api/auth/logout',


  //Temporary roles subject to change
  roles: {
    admin: 'ROLE_ADMIN'
  }
}