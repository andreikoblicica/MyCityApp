import React from 'react';
import { Card, CardContent, CardActions, Button, Typography, List, ListItem , ListItemText} from '@mui/material'
import {LocationOn,Today} from '@mui/icons-material/';
import CardMedia from "@mui/material/CardMedia";


export const EventCard = (props) =>{

    const event=props.event

    const handleEdit=()=>{
        props.updateEvent(props.event)
    }

    const handleView=()=>{
        props.viewEvent(props.event)
    }

    const handleApprove=()=>{
        props.approveEvent(props.event)
    }

    const handleDelete=()=>{
        props.deleteEvent(props.event)
    }

    return (
        <Card sx={{ maxWidth: 500, border:1, borderColor:"#bbbbbb",display: 'flex', flexDirection: 'column', height:"100%", borderRadius:'10px'  }}>
            <CardMedia
                component="img"
                alt="event img"
                height="150"
                image={event.image}
            />
            <CardContent>
                <Typography variant="h5" component="div">
                    {event.title}
                </Typography>
                <Typography variant="subtitle2" color="text.secondary" component="div">
                    {event.type}
                </Typography>
                <List sx={{mb:1}}>
                    <ListItem sx={{pb:0, pl:0}}>
                        <LocationOn sx={{color: "#152238",pr:1}}/>
                        <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {event.location}/>
                    </ListItem>

                    <ListItem sx={{pb:0, pl:0}}>
                        <Today sx={{color: "#152238",pr:1}}/>
                        <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {event.startDateTime + (event.endDateTime!=="" ? (" - "+event.endDateTime) : "")}/>
                    </ListItem>


                </List>

            </CardContent>

            <CardActions sx={{mt:"auto",justifyContent:"flex-end"}}>
                <Button size="small" onClick={handleView}>View</Button>
                {event.status==="Created"&&<Button size="small" onClick={handleApprove}>Approve</Button>}
                {event.status!=="Finished" && <Button size="small" onClick={handleEdit}>Edit</Button>}
                <Button size="small" onClick={handleDelete}>Delete</Button>
            </CardActions>
        </Card>

    )
}

