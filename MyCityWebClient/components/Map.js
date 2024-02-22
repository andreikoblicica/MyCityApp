import React from 'react';
import {GoogleMap,  MarkerF} from '@react-google-maps/api';

const Map=(props)=>{



    const initialCoordinates={
        lat:46.769544,
        lng: 23.588964
    }


    const mapContainerStyle = {
        width: '100%',
        height: '525px',
    };

    if(props.coordinates.length===0){
        return(
                <GoogleMap
                    mapContainerStyle={mapContainerStyle}
                    center={initialCoordinates}
                    zoom={12}
                >

                </GoogleMap>

        );
    }


    return(
                <GoogleMap
                    mapContainerStyle={mapContainerStyle}
                    center={props.coordinates}
                    zoom={12}
                >
                    <MarkerF position={props.coordinates}/>
                </GoogleMap>


    );
}
export default Map;