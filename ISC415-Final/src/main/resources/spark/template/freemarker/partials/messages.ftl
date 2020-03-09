<#if info??>
    <div class="alert alert-info alert-dismissible fade show" role="alert">
        ${info}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</#if>
<#if warning??>
    <div class="alert alert-warning alert-dismissible fade show" role="alert">
        <strong>Warning:</strong> ${warning}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</#if>
<#if success??>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${success}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</#if>
<#if error??>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>Error:</strong> ${error}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</#if>