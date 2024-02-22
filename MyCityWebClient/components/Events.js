import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    Grid,
    Input,
    InputLabel,
    List,
    ListItem,
    ListItemText,
    MenuItem,
    Select,
    TextField,
    Typography
} from '@mui/material'
import {Add, LocationOn, Today} from '@mui/icons-material/';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import {EventCard} from "./EventCard";
import {events} from "../services/events";
import {useNavigate} from "react-router-dom";
import {fileUploader} from "../services/fileUploader";

const Events = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const [openDialog, setOpenDialog] = useState(false)
    const [dialogTitle,setDialogTitle]=useState("")
    const [formName,setFormName]=useState("")
    const [formType,setFormType]=useState("")
    const [formDescription,setFormDescription]=useState("")
    const [formLocation,setFormLocation]=useState("")
    const [formStartDateTime,setFormStartDateTime]=useState("")
    const [formEndDateTime,setFormEndDateTime]=useState("")
    const [allEvents,setAllEvents]=useState([])
    const [filteredEvents,setFilteredEvents]=useState([])
    const [typeFilteredEvents,setTypeFilteredEvents]=useState([])
    const [filterEventTypes,setFilterEventTypes]=useState([])
    const [eventTypes,setEventTypes]=useState([])
    const [eventStatus,setEventStatus]=useState("Created")
    const [eventType,setEventType]=useState("All")

    const [formImageFile, setFormImageFile]=useState(null)
    const [formImageName, setFormImageName]=useState("")

    const [confirmDialogTitle,setConfirmDialogTitle]=useState("")
    const [confirmDialogDescription,setConfirmDialogDescription]=useState("")
    const [openConfirmDialog, setOpenConfirmDialog]=useState(false)

    const [openViewDialog, setOpenViewDialog]=useState(false)

    const [selectedEvent,setSelectedEvent]=useState([])

    const [refreshKey,setRefreshKey]=useState(0)

    const [nameError, setNameError]=useState(false)
    const [typeError, setTypeError]=useState(false)
    const [descriptionError,setDescriptionError]=useState(false)
    const [locationError, setLocationError]=useState(false)
    const [startTimeError,setStartTimeError]=useState(false)
    const [endTimeError,setEndTimeError]=useState(false)

    const [nameHelper,setNameHelper]=useState("")
    const [typeHelper,setTypeHelper]=useState("")
    const [locationHelper, setLocationHelper]=useState(false)
    const [descriptionHelper,setDescriptionHelper]=useState("")
    const [startTimeHelper,setStartTimeHelper]=useState("")
    const [endTimeHelper,setEndTimeHelper]=useState("")

    const [isFormValid,setIsFormValid]=useState(true)

    const navigate=useNavigate()


    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function getAllEvents(){
            const res= await events.findAll();
            setAllEvents(res);
            setFilteredEvents(res.filter(event=>event.status===eventStatus))
            if(eventType==='All'){
                setTypeFilteredEvents(res.filter(event=>event.status===eventStatus))
            }else{
                setTypeFilteredEvents(res.filter(event=>event.type===eventType))
            }

        }

        async function getAllTypes(){
            const res= await events.findAllTypes();
            let all=["All"]
            all.push(...res)
            setFilterEventTypes(all)
            setEventTypes(res)
        }

        getAllEvents();
        getAllTypes()
    }, [refreshKey])

    const createEvent = ()=>{
        setDialogTitle("Create Event")
        setOpenDialog(true)
    }


    const cancel=()=>{
        setOpenDialog(false)
        setFormName("")
        setFormType("")
        setFormLocation("")
        setFormDescription("")
        setFormStartDateTime("")
        setFormEndDateTime("")
        setFormImageName("")
        setFormImageFile(null)
        setNameError(false)
        setLocationError(false)
        setDescriptionError(false)
        setTypeError(false)
        setStartTimeError(false)
        setEndTimeError(false)
        setStartTimeHelper("")
        setEndTimeHelper("")
        setTypeHelper("")
        setDescriptionHelper("")
        setNameHelper("")
        setLocationHelper("")
        setIsFormValid(true)
    }


    const deleteEvent=(event)=>{
        setSelectedEvent(event)
        setConfirmDialogTitle("Delete Event?")
        setConfirmDialogDescription("Once you confirm this action, the event will be permanently deleted and will no longer be visible in the application")
        setOpenConfirmDialog(true)
    }

    const approveEvent=(event)=>{
        setSelectedEvent(event)
        setConfirmDialogTitle("Approve Event?")
        setConfirmDialogDescription("Once you confirm this action, the event submitted by "+event.username+" will be approved and will appear in the users' event section ")
        setOpenConfirmDialog(true)
    }

    const confirmDeleteEvent=()=>{
        events.delete(selectedEvent.id).then(()=>{
            setOpenConfirmDialog(false)
            setRefreshKey(refreshKey+1)

        })
    }

    const confirmApproveEvent=()=>{
        selectedEvent.status="Approved"
        events.updateStatus(selectedEvent).then(()=>{
            setOpenConfirmDialog(false)
            setRefreshKey(refreshKey+1)
        })
    }

    const submitAddEvent= async (event)=>{
        const file={
            preview: URL.createObjectURL(formImageFile),
            data: formImageFile,
        }
        fileUploader.handleSubmit(file).then(publicUrl => {
            event.image=publicUrl
            event.status="Approved"
            event.username=user.username
            events.create(event).then((response)=>{
               setRefreshKey(refreshKey+1)
            })
                .catch(()=>alert("Error creating event"));
        })

    }

    const submitUpdateEvent=(event)=>{
        event.id=selectedEvent.id
        if(formImageFile!==null){
            const file={
                preview: URL.createObjectURL(formImageFile),
                data: formImageFile,
            }
            fileUploader.handleSubmit(file).then(publicUrl => {
                event.image=publicUrl
                events.update(event).then((response)=>{
                    setRefreshKey(refreshKey+1)
                })
                    .catch(()=>alert("Error creating event"));
            })
        }else{
            event.image=formImageName
            events.update(event).then(()=>{
                setRefreshKey(refreshKey + 1)
            })
                .catch(()=>alert("Error updating event"));
        }


    }

    const validForm=()=>{
        return !nameError&&!locationError&&!typeError&&!descriptionError&&!startTimeError
            &&!endTimeError&&formName!==''&&formLocation!==''&&formDescription!==''&&formType!==''&&formStartDateTime!==''&&formImageName!==''

    }

    const submit = () =>{
        let event={
            id: null,
            title: formName,
            location: formLocation,
            description: formDescription,
            type: formType,
            startDateTime:formStartDateTime,
            endDateTime:formEndDateTime,
            image:""
        };

        if(validForm()){
            setIsFormValid(true)
            if(dialogTitle==="Create Event"){
                submitAddEvent(event);
            }else{
                submitUpdateEvent(event);
            }
            cancel();
            setOpenDialog(false);
        }
        else{
            console.log("here")
            setIsFormValid(false)
        }
    }



    const viewEvent=(event)=>{
        setSelectedEvent(event)
        setOpenViewDialog(true)
    }

    const confirm=()=>{
        if(confirmDialogTitle==="Delete Event?"){
            confirmDeleteEvent()
        }else{
            confirmApproveEvent()
        }
    }

    const closeViewDialog=()=>{
       setOpenViewDialog(false)
    }

    const updateEvent=(event)=>{
        setSelectedEvent(event)
        setOpenDialog(true)
        setFormName(event.title)
        setFormType(event.type)
        setFormLocation(event.location)
        setFormStartDateTime(convertDate(event.startDateTime))
        let endDateTime=''
        if(event.endDateTime!==''){
           endDateTime=convertDate(event.endDateTime)
        }else{
            endDateTime=''
        }
        setFormEndDateTime(endDateTime)

        setFormDescription(event.description)
        setFormImageName(event.image)
    }

    const filterEventsByStatus = async (status) =>{
        setEventStatus(status)
        setEventType("All")
        let filtered=allEvents.filter(event=>event.status===status)
        setFilteredEvents(filtered);
        setTypeFilteredEvents(filtered);
    }

    const filterEventsByType=(type)=>{
        setEventType(type)
        if(type==="All"){
            setTypeFilteredEvents(filteredEvents)
        }
        else{
            setTypeFilteredEvents(filteredEvents.filter((event)=>event.type===type))
        }
    }

    const handleNameBlur=()=>{
        let helperText=''
        let error=false
        if(formName.length<1){
            helperText="Name cannot be empty"
            error=true
        }
        setNameHelper(helperText)
        setNameError(error)
    }

    const handleLocationBlur=()=>{
        let helperText=''
        let error=false
        if(formLocation.length<1){
            helperText="Location cannot be empty"
            error=true
        }
        setLocationHelper(helperText)
        setLocationError(error)
    }

    const dateTimeRegex=/[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]/

    const handleStartTimeBlur=()=>{
        let helperText=''
        let error=false
        if(formStartDateTime.length<1){
            helperText="Start date time cannot be empty"
            error=true
        }else{
            if(!dateTimeRegex.test(formStartDateTime)){
                helperText="Start date time is not a valid format"
                error=true
            }
        }
        setStartTimeHelper(helperText)
        setStartTimeError(error)
    }

    const handleEndTimeBlur=()=>{
        let helperText=''
        let error=false
        if(formEndDateTime.length>0){
            if(!dateTimeRegex.test(formEndDateTime)){
                helperText="Start date time is not a valid format"
                error=true
            }
        }
        setEndTimeHelper(helperText)
        setEndTimeError(error)
    }

    const handleDescriptionBlur=()=>{
        let helperText=''
        let error=false
        if(formDescription.length<1){
            helperText="Description cannot be empty"
            error=true
        }
        setDescriptionHelper(helperText)
        setDescriptionError(error)
    }

    const handleTypeBlur=()=>{
        let helperText=''
        let error=false
        if(formType.length<1){
            helperText="Type cannot be empty"
            error=true
        }
        setTypeHelper(helperText)
        setTypeError(error)
    }

    const convertDate=(date)=>{
        const originalDate = new Date(date);
        const day = originalDate.getDate();
        const month = originalDate.getMonth() + 1; // Months are zero-indexed, so we add 1
        const year = originalDate.getFullYear();
        const formattedDate = `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
        const hours = originalDate.getHours();
        const minutes = originalDate.getMinutes();
        const formattedTime = `${hours < 10 ? '0' : ''}${hours}:${minutes < 10 ? '0' : ''}${minutes}`;
        return `${formattedDate} ${formattedTime}`
    }


    return (
        user &&<Box mt="90px" mb="20px" ml="290px">
            <Box mb="20px">
                <Typography
                    variant="h5"
                    fontWeight="bold"
                    color="#192841"
                >
                    Events
                </Typography>
                <Typography variant="body1" color="text.secondary" sx={{mb:"20px"}}>
                    Manage events happening in the city
                </Typography>
                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between', width:"97%",height:"55px", mb:"30px", alignItems:"center"}}>
                    <Box  sx={{display: 'flex', flexDirection:'row', justifyContent:'left', width:"60%", alignItems:"center"}}>
                        <ToggleButtonGroup
                            value={eventStatus}
                            exclusive
                            onChange={(e)=>filterEventsByStatus(e.target.value)}
                            aria-label="event option"
                            size="small"
                            color="primary"
                            sx={{mr:"25px", height:"38px", mt:"3px"}}
                        >
                            <ToggleButton value="Created" aria-label="new" sx={{border:1}} >
                                Created
                            </ToggleButton>
                            <ToggleButton value="Approved" aria-label="approved" sx={{border:1}} >
                                Approved
                            </ToggleButton>
                            <ToggleButton value="Finished" aria-label="finished" sx={{border:1}} >
                                Finished
                            </ToggleButton>
                        </ToggleButtonGroup>

                        <FormControl sx={{width:"200px"}} margin="dense" size="small">
                            <InputLabel id="eventType">Type</InputLabel>
                            <Select
                                labelId="Type"
                                value={eventType}
                                label="Type"
                                onChange={(e) => filterEventsByType(e.target.value)}
                            >
                                {filterEventTypes.map((eventType)=>{ return(
                                    <MenuItem value={eventType} key={eventType}>{eventType}</MenuItem>
                                )})}

                            </Select>
                        </FormControl>
                    </Box>

                    <Button variant="contained" onClick={()=>createEvent()}>
                        <Add /> Create
                    </Button>
                </Box>
            </Box>


                <Box width="97%">
                    <Grid container spacing={3} sx={{alignItems:'space-around'}}>

                        {
                            typeFilteredEvents.map((event) => {
                                return (

                                    <Grid item  sx={{justifyContent: 'center'}} xs={12} lg={4} key={event.id}>
                                        <EventCard event={event} updateEvent={updateEvent} viewEvent={viewEvent} deleteEvent={deleteEvent} approveEvent={approveEvent}/>
                                    </Grid>  )})
                        }
                    </Grid>
                </Box>

            <Dialog open={openDialog} >
                <DialogTitle sx={{ml:1}}>{dialogTitle}</DialogTitle>
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"500px",p:"0 35px 10px 35px"}}>
                    <TextField
                        margin="dense"
                        id="name"
                        label="Name"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={nameError}
                        onBlur={handleNameBlur}
                        helperText={nameHelper}
                        value={formName}
                        onChange={(e) => setFormName(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="location"
                        label="Location"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={locationError}
                        onBlur={handleLocationBlur}
                        helperText={locationHelper}
                        value={formLocation}
                        onChange={(e) => setFormLocation(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="startDateTime"
                        label="Start date time (yyyy-MM-dd HH:mm)"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={startTimeError}
                        onBlur={handleStartTimeBlur}
                        helperText={startTimeHelper}
                        value={formStartDateTime}
                        onChange={(e) => setFormStartDateTime(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="endDateTime"
                        label="End date time (yyyy-MM-dd HH:mm)"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={endTimeError}
                        onBlur={handleEndTimeBlur}
                        helperText={endTimeHelper}
                        value={formEndDateTime}
                        onChange={(e) => setFormEndDateTime(e.target.value)}
                    />

                    <TextField
                        multiline
                        margin="dense"
                        id="description"
                        label="Description"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={descriptionError}
                        onBlur={handleDescriptionBlur}
                        helperText={descriptionHelper}
                        value={formDescription}
                        onChange={(e) => setFormDescription(e.target.value)}
                    />



                    <FormControl fullWidth margin="dense" size="small">
                        <InputLabel id="selectType">Type</InputLabel>
                        <Select
                            labelId="selectType"
                            value={formType}
                            label="Type"
                            error={typeError}
                            onBlur={handleTypeBlur}
                            helperText={typeHelper}
                            onChange={(e) => setFormType(e.target.value)}
                        >
                            {eventTypes.map((eventType)=>{ return(
                                <MenuItem value={eventType} key={eventType}>{eventType}</MenuItem>
                            )})}
                        </Select>
                    </FormControl>

                    <Box  border={1} borderRadius={1} borderColor="#bbbbbb" height="40px" mt="10px">
                    <InputLabel htmlFor="upload-file" sx={{pl:"15px", pt:"8px"}}>
                        {formImageName==="" ? "Upload Picture" : formImageName}
                    </InputLabel>
                    <Input
                        id="upload-file"
                        type="file"
                        inputProps={{ style: { display: "none" } }}
                        onChange={e=>{setFormImageFile(e.target.files[0]);setFormImageName(e.target.files[0].name)}}
                    />

                    </Box>
                    <Typography color="red">{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancel}>Cancel</Button>
                    <Button variant="contained" onClick={submit}>Submit</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openConfirmDialog} >
                <DialogTitle>{confirmDialogTitle}</DialogTitle>
                <DialogContent>
                    {confirmDialogDescription}
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenConfirmDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={confirm}>{confirmDialogTitle==="Delete Event?" ? "Delete" : "Approve"}</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openViewDialog} maxWidth="900px">
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"900px",p:"0 0 10px 0px"}}>
                    <Box
                        component="img"
                        sx={{
                            width:"100%",
                            height:"350px"
                        }}
                        alt="Image"
                        src={selectedEvent.image}
                    />
                    <Box ml="20px" mr="20px">
                        <Typography variant="h5" component="div" sx={{mb:1}}>
                            {selectedEvent.title}
                        </Typography>
                        <List sx={{mb:1}}>
                            <ListItem sx={{pb:0, pl:0}}>
                                <LocationOn sx={{color: "#152238",pr:1}}/>
                                <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {selectedEvent.location}/>
                            </ListItem>

                            <ListItem sx={{pb:0, pl:0}}>
                                <Today sx={{color: "#152238",pr:1}}/>
                                <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {selectedEvent.startDateTime + (selectedEvent.endDateTime!=="" ? (" - "+selectedEvent.endDateTime) : "")}/>
                            </ListItem>


                        </List>
                        <Typography variant="body1" component="div">
                            {selectedEvent.description}
                        </Typography>
                    </Box>


                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={closeViewDialog}>Close</Button>
                </DialogActions>
            </Dialog>

        </Box>
    )
}

export default Events;