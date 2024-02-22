import React, {useEffect, useState} from "react";
import {Box, Toolbar, Drawer, List, ListItem, ListItemButton, ListItemText} from '@mui/material'
import {Dashboard, Article, NotificationsActive, AccountCircle, ReportProblem} from '@mui/icons-material'
import { useNavigate } from "react-router-dom";

const InstitutionSidebar = () =>{

    const [user,setUser]=useState([])

    const navigate = useNavigate();
    const drawerWidth=250
    const textColor = {
        color: "white"
    };

    useEffect(() => {
        let user = JSON.parse(sessionStorage.getItem('user'));
        if(user===null){
            navigate("/");
        }
        setUser(user)
    }, [])

    if(user===[]){
        return (<Box></Box>)
    }
    return (
        user.name!==null &&
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box',backgroundColor: "#192841"},
            }}
        >
            <Toolbar />
            <Box sx={{ overflow: 'auto' }}>
                <List>

                    <ListItem key={'Profile'} sx={{backgroundColor: "#152238", mt:-1, p:3, mb:1}}>
                        <AccountCircle fontSize="large" sx={{color: "#ffffff", pr:2}}/>
                        <ListItemText primaryTypographyProps={{ style: textColor, variant:"h6"}} primary={user.name} />
                    </ListItem>

                    <ListItem key={'Dashboard'} sx={{mb:-1}}>
                        <ListItemButton
                            onClick={()=>{navigate("/institution")}}
                        >
                            <Dashboard sx={{color: "#ffffff", pr:2}}/>
                            <ListItemText primaryTypographyProps={{ style: textColor }} primary={'Dashboard'} />
                        </ListItemButton>
                    </ListItem>

                    <ListItem key={'Issues'} sx={{mb:-1}}>
                        <ListItemButton
                            onClick={()=>{navigate("/institution/issues")}}
                        >
                            <ReportProblem sx={{color: "#ffffff", pr:2}}/>
                            <ListItemText primaryTypographyProps={{ style: textColor }} primary={'Issues'} />
                        </ListItemButton>
                    </ListItem>


                    <ListItem key={'News'} sx={{mb:-1}} >
                        <ListItemButton onClick={()=>navigate("/institution/news")}>
                            <Article sx={{color: "#ffffff", pr:2}}/>
                            <ListItemText primaryTypographyProps={{ style: textColor }} primary={'News'} />
                        </ListItemButton>
                    </ListItem>
                    <ListItem key={'Alerts'} sx={{mb:-1}} >
                        <ListItemButton onClick={()=>navigate("/institution/alerts")}>
                            <NotificationsActive sx={{color: "#ffffff", pr:2}}/>
                            <ListItemText primaryTypographyProps={{ style: textColor }} primary={'Alerts'} />
                        </ListItemButton>
                    </ListItem>

                </List>
            </Box>
        </Drawer>
    )
}

export default InstitutionSidebar;
