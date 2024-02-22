import React, {useState, useEffect} from "react";
import {
    Box,
    Button, Checkbox,
    Dialog,
    DialogActions,
    DialogContent, DialogTitle,
    FormControl, FormControlLabel,
    InputLabel, MenuItem,
    Select,
    Typography
} from '@mui/material'
import {DataGrid} from "@mui/x-data-grid";
import IconButton from "@mui/material/IconButton";
import {Delete, Visibility} from '@mui/icons-material/';
import {issues} from "../services/issues";
import {useNavigate} from "react-router-dom";
const InstitutionIssues=()=>{

    const user=JSON.parse(sessionStorage.getItem('user'))
    const institution=JSON.parse(sessionStorage.getItem('institution'))
    const [listByStatus,setListByStatus]=useState("All")
    const [allIssues,setAllIssues]=useState([])
    const [filteredIssues,setFilteredIssues]=useState([])
    const [selectedIssue,setSelectedIssue]=useState([])

    const [openDeleteDialog,setOpenDeleteDialog]=useState(false)

    const [checkboxChecked,setCheckboxChecked]=useState(false)

    const navigate=useNavigate();

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
            field: "status",
            headerName: "Status",
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
            renderCell: (params) => (
                <>
                    <IconButton aria-label="edit"  onClick={() => {
                        viewIssue(params.row)}
                    }>
                        <Visibility />
                    </IconButton>
                    <IconButton aria-label="delete"  onClick={() => deleteIssue(params.row.id)}>
                        <Delete />
                    </IconButton>
                </>
            ),
            params: (params) => ({ ...params, id: params.row }),
        },
    ]

    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function init(){

            const allIssues= await issues.findByInstitution(institution.id);

            for(let i=0;i<allIssues.length;i++){
                let involvedInstitution=allIssues.at(i).involvedInstitutions.filter(involvedInstitution=>involvedInstitution.institutionName===institution.name).at(0)
                allIssues.at(i).status=involvedInstitution.issueStatus
            }
            setAllIssues(allIssues)
            setFilteredIssues(allIssues);
        }

        init();
    }, [])

    const filterIssues = async (status) =>{
        setListByStatus(status)
        let filtered;
        if (status === 'All') {
            filtered=allIssues;
        } else {
                filtered=allIssues.filter(issue => issue.status === status)

            }
        if(checkboxChecked){
            setFilteredIssues(filtered.filter(issue=>issue.involvedInstitutions.some(involvedInstitution=>involvedInstitution.username===user.username)))
        }else{
            setFilteredIssues(filtered)
        }

    }


    const viewIssue=(issue)=>{
            navigate("/institution/issue/"+issue.id)
    }

    const deleteIssue=()=>{
        issues.delete(selectedIssue.id)
    }

    const toggleAssignedByYou= async (event)=>{
        setCheckboxChecked(event.target.checked)
        let newIssues
        if(event.target.checked){
            newIssues=filteredIssues.filter(issue=>issue.involvedInstitutions.some(involvedInstitution=>involvedInstitution.username===user.username))
            setFilteredIssues(newIssues)
        }else{
            if (listByStatus === 'All') {
                setFilteredIssues(allIssues);
            }  else {
                    setFilteredIssues(allIssues.filter(issue => issue.status === listByStatus))

            }
        }

    }






    return(
        user &&<Box mt="90px" ml="290px">
            <Box mb="20px">
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

                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'left', width:"97%", mb:2}}>
                    <FormControl margin="dense" size="small" sx={{width:"200px", mr:"20px"}}>
                        <InputLabel id="status">Status</InputLabel>
                        <Select
                            labelId="status"
                            value={listByStatus}
                            label="Type"
                            onChange={(e) => filterIssues(e.target.value)}
                            defaultValue={"All"}
                        >
                            <MenuItem value={"All"}>All</MenuItem>
                            <MenuItem value={"Opened"}>Opened</MenuItem>
                            <MenuItem value={"In Progress"}>In Progress</MenuItem>
                            <MenuItem value={"Solved"}>Solved</MenuItem>
                        </Select>
                    </FormControl>

                    <FormControl>
                        <FormControlLabel sx={{color:"text.secondary" ,width:"175px", fontWeight:"bold"}} control={<Checkbox  onChange={(e)=>toggleAssignedByYou(e)}/>} label="Assigned by you" />
                    </FormControl>
                </Box>

            </Box>


            <Box height="450px" width="97%"
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
                <DataGrid rows={filteredIssues}
                          columns={columns}
                          pageSize={10}
                          rowsPerPageOptions={[10]}
                          onRowClick={(rowInfo) =>
                          { setSelectedIssue(rowInfo.row)}}
                />
            </Box>

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
export default InstitutionIssues;