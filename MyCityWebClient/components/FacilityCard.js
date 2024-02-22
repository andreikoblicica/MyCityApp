import React from 'react';
import { Card, CardContent, CardActions, Button, Typography, Box, List, ListItem , ListItemText} from '@mui/material'
import {LocationOn} from '@mui/icons-material/';
import CardMedia from "@mui/material/CardMedia";
import {InsertLink} from "@mui/icons-material";


export const FacilityCard = (props) =>{

    const facility=props.facility

    const handleClick=()=>{
        props.updateFacility(facility)
    }

    const handleView=()=>{
        props.viewFacility(props.facility)
    }

    const handleDelete=()=>{
        props.deleteFacility(props.facility)
    }

    return (
        <Card sx={{ maxWidth: 500, border:1, borderColor:"#bbbbbb",display: 'flex', flexDirection: 'column', height:"100%", borderRadius:'10px' }}>
            <CardMedia
                component="img"
                alt="event img"
                height="150"
                image={facility.imageUrl}
            />
            <CardContent>
                <Typography variant="h5" component="div">
                    {facility.name}
                </Typography>
                <Typography variant="subtitle2" color="text.secondary" component="div">
                    {facility.type}
                </Typography>
                <List >
                    <ListItem sx={{pb:0, pl:0}}>
                        <LocationOn sx={{color: "#152238",pr:1}}/>
                        <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {facility.location}/>
                    </ListItem>

                    <ListItem sx={{pb:0, pl:0}}>
                        <InsertLink sx={{color: "#152238",pr:1}}/>
                        <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {facility.websiteLink}/>
                    </ListItem>

                </List>


            </CardContent>

            <CardActions sx={{mt:"auto",justifyContent:"flex-end"}}>
                <Button size="small" onClick={handleView}>View</Button>
                <Button size="small" onClick={handleClick}>Edit</Button>
                <Button size="small" onClick={handleDelete}>Delete</Button>
            </CardActions>
        </Card>

    )
}

