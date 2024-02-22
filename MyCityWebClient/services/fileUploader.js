export const fileUploader={
    async handleSubmit(file){
        let formData = new FormData();
        formData.append("file", file.data);
        try {
            const response = await fetch("http://localhost:5000/upload", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                const responseWithBody = await response.json();
                return responseWithBody.publicUrl;
            } else {
                console.log("File upload failed");
            }
        } catch (error) {
            console.error(error);
            // Handle the error case if necessary
        }
    }
}