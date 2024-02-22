import React, {useState, useEffect} from "react";
import {
    Box,
    Typography,
    Dialog,
    DialogTitle,
    DialogContent,
    TextField,
    DialogActions,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Grid, Input, List, ListItem, ListItemText
} from '@mui/material'
import {Add} from '@mui/icons-material/';
import {FacilityCard} from "./FacilityCard";
import {facilities} from "../services/facilities";
import {useNavigate} from "react-router-dom";
import {LocationOn,InsertLink} from "@mui/icons-material";
import {fileUploader} from "../services/fileUploader";


const Facilities = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const [selectedFacility, setSelectedFacility] = useState([])
    const [refreshKey, setRefreshKey] = useState(0);
    const [openDialog, setOpenDialog] = useState(false)
    const [dialogTitle,setDialogTitle]=useState("")
    const [formName,setFormName]=useState("")
    const [formType,setFormType]=useState("")
    const [formDescription,setFormDescription]=useState("")
    const [formLocation,setFormLocation]=useState("")
    const [formWebsite,setFormWebsite]=useState("")
    const [facilityType,setFacilityType]=useState("All")



    const [allFacilities,setAllFacilities]=useState([])
    const [filteredFacilities, setFilteredFacilities] = useState([])
    const navigate = useNavigate();

    const [nameError, setNameError]=useState(false)
    const [typeError, setTypeError]=useState(false)
    const [descriptionError,setDescriptionError]=useState(false)
    const [locationError, setLocationError]=useState(false)
    const [websiteError,setWebsiteError]=useState(false)

    const [nameHelper,setNameHelper]=useState("")
    const [typeHelper,setTypeHelper]=useState("")
    const [descriptionHelper,setDescriptionHelper]=useState("")
    const [locationHelper,setLocationHelper]=useState("")
    const [websiteHelper,setWebsiteHelper]=useState("")

    const [isFormValid,setIsFormValid]=useState(true)

    const [formImageFile, setFormImageFile]=useState(null)
    const [formImageName, setFormImageName]=useState("")


    const [openConfirmDialog, setOpenConfirmDialog]=useState(false)

    const [openViewDialog, setOpenViewDialog]=useState(false)

    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function getAllFacilities(){
            const res= await facilities.findAll();
            setAllFacilities(res);
            setFilteredFacilities(res);
        }

        getAllFacilities();
    }, [refreshKey])


    const deleteFacility =(facility)=>{
        setSelectedFacility(facility)
        setOpenConfirmDialog(true)
    }

    const confirmDeleteFacility=()=>{
        facilities.delete(selectedFacility.id).then(()=>{
            setOpenConfirmDialog(false)
            filterFacilities(facilityType)
        })
    }

    const viewFacility =(facility)=>{
        setSelectedFacility(facility)
        setOpenViewDialog(true)
    }

    const updateFacility= (facility)=>{
        setSelectedFacility(facility)
        setDialogTitle("Update Facility")
        setFormType(facility.type)
        setFormName(facility.name)
        setFormLocation(facility.location)
        setFormDescription(facility.description)
        setFormWebsite(facility.websiteLink)
        setFormImageName(facility.imageUrl)
        setOpenDialog(true)
    }


    const createFacility = () =>{
        setDialogTitle("Create Facility")
        setOpenDialog(true)
    }
    const cancel = () =>{
        setOpenDialog(false)
        setFormName("")
        setFormWebsite("")
        setFormLocation("")
        setFormType("")
        setFormDescription("")
        setFormImageName("")
        setFormImageFile(null)
        setNameError(false)
        setLocationError(false)
        setDescriptionError(false)
        setTypeError(false)
        setWebsiteError(false)
        setWebsiteHelper("")
        setTypeHelper("")
        setDescriptionHelper("")
        setNameHelper("")
        setLocationHelper("")
        setIsFormValid(true)
    }
    const validForm=()=>{
        return !nameError&&!locationError&&!typeError&&!descriptionError&&!websiteError&&formName!==''&&formLocation!==''&&formDescription!==''&&formType!==''&&formWebsite!==''&&formImageName!==''

    }

    const submitAddFacility=(facility)=>{
            const file={
                preview: URL.createObjectURL(formImageFile),
                data: formImageFile,
            }
            fileUploader.handleSubmit(file).then(publicUrl => {
                facility.imageUrl=publicUrl
                facilities.create(facility).then((response)=>{
                    setRefreshKey(refreshKey + 1)
                })
                    .catch(()=>alert("Error creating facility"));
            })



    }

    const submitUpdateFacility=(facility)=>{
        facility.id=selectedFacility.id
        console.log(facility)
        if(formImageFile!==null){
            const file={
                preview: URL.createObjectURL(formImageFile),
                data: formImageFile,
            }
            fileUploader.handleSubmit(file).then(publicUrl => {
                facility.imageUrl=publicUrl
                facilities.update(facility).then(()=>{
                    setRefreshKey(refreshKey + 1)
                })
                    .catch(()=>alert("Error updating facility"));
            })
        }else{
            facility.imageUrl=formImageName
            facilities.update(facility).then(()=>{
                setRefreshKey(refreshKey + 1)
            })
                .catch(()=>alert("Error updating facility"));
        }


    }

    const submit = () =>{
        let facility={
            id: null,
            type: formType,
            name: formName,
            description: formDescription,
            location: formLocation,
            websiteLink:formWebsite,
            imageUrl:""
        };

        if(validForm()){
            setIsFormValid(true)
            if(dialogTitle==="Create Facility"){
                submitAddFacility(facility);
            }else{
                submitUpdateFacility(facility);
            }
            cancel();
            setOpenDialog(false);
        }
        else{
            setIsFormValid(false)
        }
    }

    const filterFacilities = async (type) =>{
        setFacilityType(type)

        if (type === 'All') {
            const res= await facilities.findAll();
            setAllFacilities(res)
            setFilteredFacilities(res)
        } else {
            const res= await facilities.findByType(type);
            setFilteredFacilities(res);
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

    const handleWebsiteBlur=()=>{
        let helperText=''
        let error=false
        const websiteRegex=/^https?:\/\/(?:www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&\/=]*)$/
        if(allFacilities.find(facility => facility.websiteLink === formWebsite) && !(dialogTitle==="Update Facility" && formWebsite===selectedFacility.websiteLink)){
            helperText="Website link already exists"
            error=true
        }
        if(!websiteRegex.test(formWebsite)){
            helperText="Website link is not valid"
            error=true
        }
        setWebsiteError(error)
        setWebsiteHelper(helperText)
    }

    const closeViewDialog=()=>{
        setOpenViewDialog(false)
    }

    return (
        user &&<Box mt="90px" ml="290px">
            <Box mb="20px">
                <Typography
                    variant="h5"
                    fontWeight="bold"
                    color="#192841"
                >
                    Facilities
                </Typography>
                <Typography sx={{mb:2}} variant="body1" color="text.secondary"  >
                    Manage Facility Information
                </Typography>
                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'space-between', width:"97%", mb:2}}>
                    <FormControl margin="dense" size="small" sx={{width:"150px"}}>
                        <InputLabel id="facilityType">Type</InputLabel>
                        <Select
                            labelId="facilityType"
                            value={facilityType}
                            label="Type"
                            onChange={(e) => filterFacilities(e.target.value)}
                            defaultValue={"All"}
                        >
                            <MenuItem value={"All"}>All</MenuItem>
                            <MenuItem value={"Park"}>Park</MenuItem>
                            <MenuItem value={"Mall"}>Mall</MenuItem>
                            <MenuItem value={"Library"}>Library</MenuItem>
                            <MenuItem value={"Cinema"}>Cinema</MenuItem>
                            <MenuItem value={"Sports Facility"}>Sports Facility</MenuItem>
                            <MenuItem value={"Museum"}>Museum</MenuItem>
                            <MenuItem value={"Attraction"}>Attraction</MenuItem>
                        </Select>
                    </FormControl>
                    <Button variant="contained" sx={{height:38}} onClick={()=>createFacility()}>
                        <Add /> Create
                    </Button>
                </Box>


            </Box>


            <Box width="97%" mb={2}>
                <Grid container spacing={3} sx={{alignItems:'space-around'}}>

                    {
                        filteredFacilities.map((facility) => {
                            return (

                                <Grid item  sx={{justifyContent: 'center'}} xs={12} lg={4} key={facility.id}>
                                    <FacilityCard facility={facility} updateFacility={updateFacility}  viewFacility={viewFacility} deleteFacility={deleteFacility}/>
                                </Grid>  )})
                    }
                </Grid>
            </Box>


            <Dialog open={openDialog} maxWidth="900px">
                <DialogTitle sx={{ml:1}}>{dialogTitle}</DialogTitle>
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"700px",p:"0 35px 10px 35px"}}>
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
                        id="website"
                        label="Website"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={websiteError}
                        onBlur={handleWebsiteBlur}
                        helperText={websiteHelper}
                        value={formWebsite}
                        onChange={(e) => setFormWebsite(e.target.value)}
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



                    <FormControl fullWidth margin="dense" size="small" sx={{mb:"15px"}}>
                        <InputLabel id="selectType">Type</InputLabel>
                        <Select
                            labelId="selectType"
                            value={formType}
                            label="Type"
                            onChange={(e) => setFormType(e.target.value)}
                        >
                            <MenuItem value={"Park"}>Park</MenuItem>
                            <MenuItem value={"Mall"}>Mall</MenuItem>
                            <MenuItem value={"Library"}>Library</MenuItem>
                            <MenuItem value={"Cinema"}>Cinema</MenuItem>
                            <MenuItem value={"Sports Facility"}>Sports Facility</MenuItem>
                            <MenuItem value={"Museum"}>Museum</MenuItem>
                            <MenuItem value={"Attraction"}>Attraction</MenuItem>
                        </Select>
                    </FormControl>

                    <Box  border={1} borderRadius={1} borderColor="#aaaaaa" height="40px">
                    <InputLabel htmlFor="upload-file"  sx={{pl:"15px", pt:"8px"}}>
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
                <DialogTitle>Delete facility?</DialogTitle>
                <DialogContent>
                    Once you confirm this action, the facility will be permanently deleted and will no longer be visible in the application
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenConfirmDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={confirmDeleteFacility}>Delete</Button>
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
                        src={selectedFacility.imageUrl}
                    />
                    <Box ml="20px" mr="20px">
                        <Typography variant="h5" component="div" sx={{mb:1}}>
                            {selectedFacility.name}
                        </Typography>
                        <List sx={{mb:1}}>
                            <ListItem sx={{pb:0, pl:0}}>
                                <LocationOn sx={{color: "#152238",pr:1}}/>
                                <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {selectedFacility.location}/>
                            </ListItem>

                            <ListItem sx={{pb:0, pl:0}}>
                                <InsertLink sx={{color: "#152238",pr:1}}/>
                                <ListItemText primaryTypographyProps={{  variant:"body2"}} primary= {selectedFacility.websiteLink}/>
                            </ListItem>


                        </List>
                        <Typography variant="body1" component="div">
                            {selectedFacility.description}
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

export default Facilities;