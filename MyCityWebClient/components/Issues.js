import React, {useState, useEffect} from "react";
import {
    Box,
    Typography,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    List, ListItem
} from '@mui/material'
import { DataGrid } from "@mui/x-data-grid";
import IconButton from '@mui/material/IconButton';
import {Delete, Visibility} from '@mui/icons-material/';

import {issues} from "../services/issues";
import {useNavigate} from "react-router-dom";
import { EventNote, LocationOn, ReportProblem} from "@mui/icons-material";
import {GoogleMap, MarkerF} from "@react-google-maps/api";



const Issues = () =>{


    const user = JSON.parse(sessionStorage.getItem('user'));
    const [openDialog, setOpenDialog] = useState(false)
    const [selectedIssue,setSelectedIssue]=useState([])
    const [refreshKey, setRefreshKey]=useState(0)


    const [allIssues,setAllIssues]=useState([])

    const [openDeleteDialog,setOpenDeleteDialog]=useState(false)
    const navigate = useNavigate();

    const [coordinates,setCoordinates]=useState({lat:46.775503860000555,lng:23.606087677180767})



    const columns=[
        {
            field: "id",
            headerName: "ID",
            flex:0.05
        },
        {
            field: "username",
            headerName: "User",
            flex:0.15
        },

        {
            field: "type",
            headerName: "Type",
            flex:0.2
        },
        {
            field: "title",
            headerName: "Title",
            flex:0.2
        },
        {
            field: "location",
            headerName: "Location",
            flex:0.2
        },
        {
            field: "dateTime",
            headerName: "Date",
            flex:0.1
        },
        {
            field: 'actions',
            headerName: 'Actions',
            flex:0.1,
            sortable: false,
            renderCell: () => (
                <>
                    <IconButton aria-label="edit"  onClick={() => {
                        viewIssue()}
                    }>
                        <Visibility />
                    </IconButton>
                    <IconButton aria-label="delete"  onClick={()=>setOpenDeleteDialog(true)}>
                        <Delete />
                    </IconButton>
                </>
            ),
            params: (params) => ({ ...params, id: params.row }),
        },
    ]

    useEffect(()=>{
        if(!user){
            navigate("/")
        }
        async function getAllIssues(){
            const res= await issues.findAll();
            setAllIssues(res);
            setSelectedIssue(res.length>0 ? res.at(0) : [])
        }

        getAllIssues();
    },[refreshKey])

    useEffect(()=>{

        let str=selectedIssue.coordinates
        if(typeof str === 'string'){
            const [latitude,longitude]=str.split(",")
            const coordinates={
                lat: parseFloat(latitude),
                lng: parseFloat(longitude)
            }
            setCoordinates(coordinates)
        }


    },[selectedIssue])

    const deleteIssue=()=>{
        issues.delete(selectedIssue.id).then(()=>setRefreshKey(refreshKey+1))
        setOpenDialog(false)
        setOpenDeleteDialog(false)
    }

     const  viewIssue=()=>{
        setOpenDialog(true)
    }


    const close = () =>{
        setOpenDialog(false)
    }


    return (
        user && <Box mt="90px" ml="290px">
            <Box mb="20px">
                <Typography
                    variant="h5"
                    fontWeight="bold"
                    color="#192841"
                >
                    Issues
                </Typography>
                <Typography variant="body1" color="text.secondary"  >
                    Manage issues submitted by users
                </Typography>

            </Box>


            <Box mt="56px" height="475px" width="97%"
                 sx={{
                     "& .MuiDataGrid-root": {
                         border: "1px solid #aaaaaa !important",
                     },
                     "& .MuiDataGrid-cell": {
                         borderBottom: "1px solid #bbbbbb !important",

                     },
                     "& .MuiDataGrid-columnHeaders": {
                         borderBottom: "1px solid #aaaaaa !important",
                         // backgroundColor: "#cccccc",
                     },
                 }}
            >
                <DataGrid rows={allIssues}
                          columns={columns}
                          pageSize={10}
                          rowsPerPageOptions={[10]}
                          onRowClick={(rowInfo) =>
                          { setSelectedIssue(rowInfo.row)}}
                       />
            </Box>


            <Dialog open={openDialog} maxWidth="900px">
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"600px",p:"0 0 10px 0px"}}>
                    <Box
                        component="img"
                        sx={{
                            width:"100%",
                            height:"300px"
                        }}
                        alt="Image"
                        src={selectedIssue.image}
                    />
                    <Box ml="20px" mr="20px">
                        <Typography variant="body1" color="text.primary" >
                            {selectedIssue.type}
                        </Typography>
                        <Typography variant="h5" fontWeight="bold">
                            {selectedIssue.title}
                        </Typography>


                        <List>
                            <ListItem>
                                <ReportProblem sx={{mr:"5px", ml:"-15px"}}/>
                                <Typography variant="body1"  color="text.primary" alignContent="center" >
                                    {selectedIssue.status}
                                </Typography>
                            </ListItem>
                            <ListItem>
                                <EventNote sx={{mr:"5px", ml:"-15px"}}/>
                                <Typography variant="body1"  color="text.primary" alignContent="center" >
                                    {selectedIssue.dateTime}
                                </Typography>
                            </ListItem>
                            <ListItem>
                                <LocationOn sx={{mr:"5px", ml:"-15px"}}/>
                                <Typography variant="body1"  color="text.primary" alignContent="center" >
                                    {selectedIssue.location}
                                </Typography>
                            </ListItem>
                        </List>



                        <Typography variant="body1" color="text.primary" sx={{mb:2}}>
                            {selectedIssue.description}
                        </Typography>

                        <Typography variant="body1" color="text.primary" fontWeight="bold" sx={{mb:1}}>
                            Involved institutions:
                        </Typography>




                        {
                            selectedIssue.involvedInstitutions?.map((institution)=>
                            {return(
                                <Box sx={{display:'flex',flexDirection:'row'}}>
                                    <Typography variant="body1" color="text.primary" width="270px">
                                        {institution.institutionName}:
                                    </Typography>
                                    <Typography variant="body1" color="text.primary" sx={{mb:1}}>
                                        {selectedIssue.status}
                                    </Typography>
                                </Box>
                            )}
                            )
                        }

                        <Typography variant="body1" color="text.primary" fontWeight="bold" sx={{mb:1}}>
                            Location:
                        </Typography>


                            <Box>
                                <GoogleMap
                                    mapContainerStyle={{ width: '550px', height: '350px'}}
                                    center={coordinates}
                                    zoom={12}
                                >
                                    <MarkerF position={coordinates}/>
                                </GoogleMap>
                            </Box>







                    </Box>
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={close}>Close</Button>
                    <Button variant="contained" onClick={deleteIssue}>Delete</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openDeleteDialog} >
                <DialogTitle>Delete issue?</DialogTitle>
                <DialogContent>
                    Are you sure you want to permanently delete issue #{selectedIssue.id}? In case you shared it with other institutions, it will also be deleted from their page.
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenDeleteDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={deleteIssue}>Delete</Button>
                </DialogActions>
            </Dialog>


        </Box>


    )
}

export default Issues;