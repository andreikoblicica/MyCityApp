import React from "react";
import {AppBar, Toolbar, Typography, Button} from '@mui/material'
import {auth} from "../services/auth"
import {useNavigate} from "react-router-dom";
const Appbar = () =>{

    const navigate=useNavigate()
    return (
        <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1  }}>
            <Toolbar>
                <Typography variant="h5" component="div" sx={{ flexGrow: 1 }}>
                    Community App
                </Typography>
                <Button color="inherit" onClick={(e)=>{auth.logout();navigate("/")}}>Log out</Button>
            </Toolbar>
        </AppBar>
    )
}

export default Appbar;

