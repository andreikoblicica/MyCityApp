import authHeader, { BASE_URL, HTTP } from "./http";

export const institutions = {
    findAll() {
        return HTTP.get(BASE_URL + "/admin/institutions",{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    findByUserId(id) {
        return HTTP.get(BASE_URL + "/institution/"+id,{ headers: authHeader() } ).then(
            (response) => {
                return response.data;
            }
        );
    },
    create(institution) {
        return HTTP.post(BASE_URL + "/admin/institutions", institution, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },
    update(institution) {
        return HTTP.put(BASE_URL + "/admin/institutions" , institution, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    updateInvolvedInstitution(involvedInstitution) {
        return HTTP.post(BASE_URL + "/institution/issue", involvedInstitution, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    reassignIssue(involvedInstitution,id) {
        return HTTP.post(BASE_URL + "/institution/issue/reassign/"+id,involvedInstitution, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    shareIssue(involvedInstitutions,id) {
        return HTTP.post(BASE_URL + "/institution/issue/share/"+id,involvedInstitutions, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },
    delete(id) {
        return HTTP.delete(BASE_URL + "/admin/institutions/"+ id, {
            headers: authHeader(),
        }).then((response) => {
            return response.data;
        });
    },

    existsEmail(email) {
        return HTTP.post(BASE_URL + "/admin/institutions/existsemail/"+email, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },

    existsWebsite(website) {
        return HTTP.post(BASE_URL + "/admin/institutions/existswebsite",website, { headers: authHeader() }).then(
            (response) => {
                return response.data;
            }
        );
    },

};