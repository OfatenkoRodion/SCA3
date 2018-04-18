$(document).ready(function () {
    var lock = new Auth0LockPasswordless(AUTH0_CLIENT_ID, AUTH0_DOMAIN, {
        allowedConnections: ['sms'],
        auth: {
            redirectUrl: AUTH0_CALLBACK_URL,
            responseType: 'code'
        }
    });

    $('.btn-login').click(function (e) {
        e.preventDefault();
        lock.show();
    });
});