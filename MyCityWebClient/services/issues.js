import authHeader, { BASE_URL, HTTP } from "./http";

export const issues = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/issues",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByInstitution(id){
        return HTTP.get(BASE_URL + "/institution/issues/"+id,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findById(id){
        return HTTP.get(BASE_URL + "/institution/issue/"+id,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByInvolvedUser(id,username){
        return HTTP.get(BASE_URL + "/institution/issues/"+id+"/"+username,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(issue){
        return HTTP.post(BASE_URL + "/admin/issues",issue,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    update(issue){
        return HTTP.post(BASE_URL + "/institution/issue/update-status",issue,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/issues/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

    sendMessage(message){
        return HTTP.post(BASE_URL+"/send", message,{ headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },



};