import { BASE_URL, HTTP } from "./http";



export const auth={
      login(data) {


        return HTTP.post(BASE_URL + "/auth/login", data).then((response) => {
            if (response.data.token) {
                sessionStorage.setItem("user", JSON.stringify(response.data));

            }

            return response.data;
        });
    },

     logout() {
        localStorage.removeItem("user");

    },

     register(data){
        return HTTP.post(BASE_URL + "/auth/sign-up", data).then(
            (response) => {
                return response;
            }
        )
    }
}
