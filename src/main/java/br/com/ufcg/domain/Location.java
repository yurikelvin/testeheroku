package br.com.ufcg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TAB_LOCATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {
	
		@Id
	    @GeneratedValue(strategy= GenerationType.AUTO)
	    @Column(name = "ID_LOCATION")
	    private Long id;
		
		@Column(name = "TX_LAT", nullable = false)
		private Double lat;
		
		@Column(name = "TX_LNG", nullable = false) 
		private Double lng;
		
		public Location() {}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getLat() {
			return lat;
		}

		public void setLat(Double lat) {
			this.lat = lat;
		}

		public Double getLng() {
			return lng;
		}

		public void setLng(Double lng) {
			this.lng = lng;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((lat == null) ? 0 : lat.hashCode());
			result = prime * result + ((lng == null) ? 0 : lng.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Location other = (Location) obj;
			if (lat == null) {
				if (other.lat != null)
					return false;
			} else if (!lat.equals(other.lat))
				return false;
			if (lng == null) {
				if (other.lng != null)
					return false;
			} else if (!lng.equals(other.lng))
				return false;
			return true;
		}

		
		
		
	 

}
