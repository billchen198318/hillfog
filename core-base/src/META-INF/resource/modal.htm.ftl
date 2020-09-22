<div class="modal fade" role="dialog" aria-labelledby="modalLabel-${prog.progId}" aria-hidden="true" id="modal-${prog.progId}">
  <div class="modal-dialog modal-lg" >
    <div class="modal-content" style="width:${prog.dialogW}px; height:${prog.dialogH}px;" >
    
      <div class="modal-header">     
        	<h4 class="modal-title" id="modalLabel-${prog.progId}">${prog.name}</h4>
			<button type="button" class="close" data-dismiss="modal" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>		
      </div>
      
      <div class="modal-body">
		  <div class="container-fluid">
			<div class="row" style="width:${prog.dialogW-30}px; height:${prog.dialogH-160}px;"  >
				<iframe id="modal-iframe-${prog.progId}" src=""  frameborder="0"></iframe>
			</div>
			</div>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>

    </div>
  </div>
</div>