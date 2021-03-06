Vue.component("host-mapChoose", {
    data() {
        return {
            startDateForHost: null,
            endDateForHost: null,
            amenities: [],          // need it for adding form of new apartment ( we need available amenities )
            newApartment: {
                apartmentAmentitiesIDs: [],
                apartmentCommentsIDs: [],
                availableDates: [],
                datesForHosting: [],
                hostID: 1,
                id: 98989,
                imagesPath: "empty",
                listOfReservationsIDs: [],
                location: {
                    address: {
                        number: null,
                        populatedPlace: '',
                        street: null,
                        zipCode: null
                    },
                    latitude: null,
                    longitude: null
                },
                logicalDeleted: 0,
                numberOfGuests: null,
                numberOfRooms: null,
                pricePerNight: null,
                status: "INACTIVE",
                timeForCheckIn: "14:00",
                timeForCheckOut: "10:00",
                typeOfApartment: null,
            },
            previewMap: false,
            codeForImage: ''
        }
    },
    template: `
    <div>
        <div class="addingNewApartment">
            <h1 style="color:black;"> Adding new apartment </h1>
            <!-- Form for adding apartment -->
            <form method='post'>

                <table class="tableForAddingNewApartment">
                    <tr>
                        <th> Label </th>
                        <th> Data </th>
                    </tr>

                    <tr>
                        <td> Type of apartment </td>
                        <td><input type="text" v-model="newApartment.typeOfApartment" placeholder="Type of apartment ..."></td>
                    </tr>

                    <tr>
                        <td> Check in </td>
                        <td><input  name="checkIn" type="time" v-model="newApartment.timeForCheckIn" placeholder="Check in ..."></td>
                    </tr>

                    <tr>
                        <td> Check out </td>
                        <td> <input name="checkOut"  type="time" v-model="newApartment.timeForCheckOut" placeholder="Check out ..."></td>
                    </tr>

                    <tr>
                        <td> Start date </td>
                        <td><input name="startDate" type="date" min="2020-07-09" max="2020-08-09" v-model="startDateForHost" placeholder="Start date for hosting ..." ></td>
                    </tr>

                    <tr>
                        <td> End date </td>
                        <td><input name="endDate" type="date" min="2020-07-09" max="2020-08-09" v-model="endDateForHost" placeholder="End date for hosting ..."></td>
                    </tr>

                    <tr>
                        <td> Price per night </td>
                        <td><input  type="number" v-model="newApartment.pricePerNight" min="1" max="5000" placeholder="Price per night ..." ></td>
                    </tr>

                    <tr>
                        <td> Number of rooms </td>
                        <td><input  type="number" v-model="newApartment.numberOfRooms" min="1" max="20" placeholder="Number of rooms ..." ></td>
                    </tr>

                    <tr>
                        <td> Max guests in room </td>
                        <td><input  type="number" v-model="newApartment.numberOfGuests" min="1" max="100" placeholder="Max guests in room ..." ></td>
                    </tr>

                    <tr>
                        <td> Location </td>
                        <td><button type="button" @click="previewMapChooseLocation()"><i class="fa fa-map-marker" aria-hidden="true"></i> Choose on map </button></td>
                    </tr>

                    <tr>
                        <td> Town name </td>
                        <td> <input type="text" id="townID" placeholder="Town name ..."> </td>
                    </tr>

                    <tr>
                        <td> Street </td>
                        <td><input type="text" id="streetID" placeholder="Street ..."></td>
                    </tr>

                    <tr>
                        <td> Number </td>
                        <td><input type="text" id="numberID" placeholder="Number ..."> </td>
                    </tr>

                    <tr>
                        <td> Zipcode </td>
                        <td> <input type="text" id="zipcodeID" placeHolder="Zipcode ..."></td>
                    </tr>

                    <tr>
                        <td> Amenities in apartment </td>
                        <td> 
                            <!-- List of amenities in apartments -->
                            <select v-model="newApartment.apartmentAmentitiesIDs" multiple>
                                <option v-for="option in amenities" v-bind:value="option.id">
                                    {{ option.itemName }}
                                </option>
                            </select>
                            <!-- End list of amenities in apartments -->
                        </td>
                    </tr>

                    <tr>
                        <td> Image  </td>
                        <td> 
                            <!-- Choose image of apartment -->
                            <input type="file" onchange="encodeImageFileAsURL(this)" /><br><br>
                            <!-- End of choose of image of apartment --> <br><br>
                        </td>
                    </tr>

                </table>

                <br><br>
                <button type="button" @click="confirmAdding()" class="saveChanges" ><i class="fa fa-check" aria-hidden="true"></i>Confirm</button> <br><br>
                 

                <!-- I use those inputs field for geting data from map -->
                <input type="text" id="latitudeID" hidden> 
                <input type="text" id="longitudeID" hidden>
                <!-- End of getind data for long i lat -->


            </form>
            <!-- End of form for adding apartment -->

            <div id="map" class="map" v-if="previewMap">  </div> 
        </div>
        <br><br>
        
        
        <img id="imgSampleID" src="" alt="Image of apartment" width="300" height="200" hidden> 

        <br><br>
        <button type="button" @click="endAddingApartments()" hidden > End adding apartments </button>

    </div>
    `,
    methods: {
        previewMapChooseLocation: function () {
            this.previewMap = !this.previewMap;
            if (this.previewMap) {
                // Draw map on screen
                this.$nextTick(function () {
                    this.initForMap();

                    // Seting some extra style for map
                    let c = document.getElementById("map").childNodes;
                    c[0].style.borderRadius  = '10px';
                    c[0].style.border = '4px solid lightgrey';
                })
            }
        },
        confirmAdding: function () {
            // Get data from form
            this.newApartment.location.address.populatedPlace = document.getElementById("townID").value;
            this.newApartment.location.address.street = document.getElementById("streetID").value;
            this.newApartment.location.address.number = document.getElementById("numberID").value;
            this.newApartment.location.address.zipCode = document.getElementById("zipcodeID").value;

            this.newApartment.location.latitude = document.getElementById("latitudeID").value;
            this.newApartment.location.longitude = document.getElementById("longitudeID").value;

            // Get codeForImage 
            this.codeForImage = document.getElementById("imgSampleID").src;
            this.newApartment.imagesPath = this.codeForImage;

            // warning/error if some fields are null or empty
            // ref: https://stackoverflow.com/questions/5515310/is-there-a-standard-function-to-check-for-null-undefined-or-blank-variables-in
            if (!this.newApartment.timeForCheckIn || !this.newApartment.timeForCheckOut ||
                !this.newApartment.pricePerNight || !this.newApartment.numberOfRooms ||
                !this.newApartment.numberOfGuests || !this.newApartment.location.address.populatedPlace ||
                !this.newApartment.location.address.street || !this.newApartment.location.address.number
            ) {

                toastr["warning"]("All field is required", "Watch out !");
                return;
            }

            // TODO: Check for empty fields
            axios
                .post('rest/apartments/addNewApartments', {
                    addedApartment: this.newApartment,
                    "startDateForReservation": this.startDateForHost,
                    "endDateForReservation": this.endDateForHost
                })
                .then(response => {
                    toastr["success"]("You make success adding !!", "Success adding!");
                    return this.apartments;
                });

        },
        endAddingApartments: function () {

            window.location.href = "http://localhost:8080/Apartments/hostDashboard.html#/hostActiveApartments";

        },
        initForMap: function () {

            const map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: [0, 0],
                    zoom: 2
                })
            })

            map.on('click', function (evt) {
                console.log(evt.coordinate);
                //alert(evt.coordinate);

                var coord = ol.proj.toLonLat(evt.coordinate);
                reverseGeocode(coord);

            })

        },



    },
    mounted() {
        this.$nextTick(function () {
            this.initForMap();
        })

        axios
            .get('rest/amenities/getAmenities')
            .then(response => {
                this.amenities = [];
                response.data.forEach(el => {
                    this.amenities.push(el);
                });
                return this.amenities;
            });


    }


});


/**
 * From coords get real address and put that value in form. 
 * @param coords cords (x,y)
 */
function reverseGeocode(coords) {
    fetch('http://nominatim.openstreetmap.org/reverse?format=json&lon=' + coords[0] + '&lat=' + coords[1])
        .then(function (response) {
            return response.json();
        }).then(function (json) {
            // LATITUDE & LONGITUDE
            console.log(coords);
            document.getElementById("longitudeID").value = coords[0];
            document.getElementById("latitudeID").value = coords[1];

            // TOWN 
            console.log(json.address);
            if (json.address.city) {
                document.getElementById("townID").value = json.address.city;
            } else if (json.address.city_district) {
                document.getElementById("townID").value = json.address.city_district;
            }

            // STREET
            if (json.address.road) {
                document.getElementById("streetID").value = json.address.road;
            }

            // NUMBER OF HOUSE
            if (json.address.house_number) {
                document.getElementById("numberID").value = json.address.house_number;
            }

            // ZIP CODE
            if(json.address.postcode){
                document.getElementById("zipcodeID").value = json.address.postcode;
            }

        });
}

/**
 * ref: https://stackoverflow.com/questions/6150289/how-can-i-convert-an-image-into-base64-string-using-javascript
 * @param {*} element 
 */
function encodeImageFileAsURL(element) {
    var file = element.files[0];
    var reader = new FileReader();
    reader.onloadend = function () {
        console.log('RESULT', reader.result);
        document.getElementById('imgSampleID')
            .setAttribute(
                'src', reader.result
            );
    }
    reader.readAsDataURL(file);
}