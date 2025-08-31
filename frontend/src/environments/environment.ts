export const environment = {
  production: false,
  apiUrl: '/api',
  apiUrl2: '/api-public',
  urlServer: 'http://localhost:8080/api',
  urlLogout: 'https://localhost:8080/api/auth/logout',
  urlLogin: '/auth/login',
  urlSendCodeEmail: '/auth/sendCodeEmail',
  urlRecoverPassword: '/auth/recover-password',
  urlUpdatePassword: '/auth/updatePassword',


  //Temporary roles subject to change
  roles: {
    admin: 'ROLE_ADMIN'
  }
}