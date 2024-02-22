import authHeader, { BASE_URL, HTTP } from "./http";

export const users = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/users",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(user) {
        return HTTP.post(BASE_URL + "/admin/users", user, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    createInstitutionUser(user,institution) {
        return HTTP.post(BASE_URL + "/admin/users/"+institution, user, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    update(user) {
        return HTTP.put(BASE_URL + "/admin/users" , user, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/users/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

    existsUsername(username) {
        return HTTP.post(BASE_URL + "/admin/users/existsusername/"+username, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },

    existsEmail(email) {
        return HTTP.post(BASE_URL + "/admin/users/existsemail/"+email, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },

    // sendMessage(message){
    //     return HTTP.post(BASE_URL+"/send", message,{ headers: authHeader() }).then(
    //         (response) => {
    //             return response.data;
    //         }
    //     );
    // },
    //
    // sendStatus(status){
    //     return HTTP.post(BASE_URL+"/status", status,{ headers: authHeader() }).then(
    //         (response) => {
    //             return response.data;
    //         }
    //     );
    // }


};