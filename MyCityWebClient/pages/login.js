import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import {auth} from "../services/auth";
import {Box, Button, Card, TextField, Typography} from "@mui/material";
import {users} from "../services/users";
import {institutions} from "../services/institutions";
function Login(){

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [visibility,setVisibility]=useState('hidden')



    const navigate = useNavigate();

    function attemptLogIn(e){
        e.preventDefault();
        let loginRequest={
            username: username,
            password: password
        }
        auth.login(loginRequest).then(
            (user) => {
                if(user.role==="Admin"){
                    navigate("/admin",{state: user});

                }
                else if(user.role==="Institution User"){
                    navigate("/institution",{state: user});

                }
            },
            (error)=>{
                setVisibility('visible');
            }
        );
    }

    useEffect(() => {
        setVisibility('hidden')
    }, [])



    return (
        // <div className="App">
        //     <div className="login-form">
        //         <h2 className="title">Community App</h2>
        //         <div className="form">
        //
        //             <form>
        //                 <div className="row-container">
        //                     <label>Username:
        //
        //                     </label>
        //                     <input
        //                         type="text"
        //                         value={username}
        //                         onChange={(e) => setUsername(e.target.value)}
        //                     />
        //                 </div>
        //                 <div className="row-container">
        //                     <label>Password:
        //                     </label>
        //                     <input
        //                         type="password"
        //                         value={password}
        //                         onChange={(e) => setPassword(e.target.value)}
        //
        //                     />
        //                 </div>
        //                 <div className="row-container">
        //                     <button type="login" onClick={attemptLogIn}>Log In</button>
        //                     <button type="login" onClick={attemptSignUp}>Sign Up</button>
        //                 </div>
        //             </form>
        //         </div>
        //     </div>
        // </div>
        <Box height="100vh" width="100%" sx={{
            display: 'flex',
            flexDirection:'column',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundColor: "#0093E9", backgroundImage: 'linear-gradient(160deg, #0093E9 0%, #80D0C7 100%)'
        }}>
            <Card sx={{height:"260px",width:"320px", p:"30px",border:1, borderColor:"#bbbbbb", borderRadius:"10px"}}>
                <Box sx={{display:'flex',flexDirection:'column', alignItems:'center'}}>
                    <Typography  variant="h4" fontWeight="bold" color="#1167b1">
                        Community App
                    </Typography>
                    <Typography  variant="h5" fontWeight="bold" color="#1167b1" sx={{mb:"10px"}}>
                        Cluj-Napoca
                    </Typography>
                </Box>

                <TextField
                    margin="dense"
                    id="username"
                    label="Username"
                    type="text"
                    fullWidth
                    variant="outlined"
                    autoComplete="off"
                    size="small"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <TextField
                    margin="dense"
                    id="password"
                    label="Password"
                    type="password"
                    fullWidth
                    autoComplete="off"
                    variant="outlined"
                    size="small"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <Box sx={{display:'flex',flexDirection:'column', alignItems:'center'}}>
                    <Button variant="contained" sx={{mt:'10px', width:'100%', height:'45px'}} onClick={attemptLogIn}>
                        Log In
                    </Button>
                    <Typography visibility={visibility} sx={{mt:'10px',mb:'10px'}} variant="body1"  color="#ff0000">
                        Incorrect credentials!
                    </Typography>
                </Box>



            </Card>
            <Box sx={{display:'flex', alignItems:'center', mt:'10px'}}>
                <Typography  variant="body1"  color="#ffffff">
                    To register your institution, contact communityappcluj@gmail.com
                </Typography>
            </Box>
        </Box>


    );
}

export default Login;