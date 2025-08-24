export const environment = {
  production: true,
  apiUrl: '/api',
  apiUrl2: '/api-public',
  urlServer: 'https://stg-belat.railway.app',
  urlLogout: 'https://keycloak-dev-24be.up.railway.app/realms/belat/protocol/openid-connect/logout?id_token_hint=',

  //Temporary roles subject to change
  roles: {
    //Roles with more permissions will be added here ()
    admin: 'ROLE_ORIGINACION_ADMIN'
  }
}